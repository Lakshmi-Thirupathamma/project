package com.capgemini.jdbcproject.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.capgemini.jdbcproject.dto.Books;
import com.capgemini.jdbcproject.dto.BorrowBooks;
import com.capgemini.jdbcproject.dto.IssueStatus;
import com.capgemini.jdbcproject.dto.RequestBook;
import com.capgemini.jdbcproject.dto.User;
import com.capgemini.jdbcproject.exception.LMSException;

public class UserDaoImp implements UserDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Statement stmt = null;

	@Override
	public boolean register(User user) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("insert into user values(?,?,?,?,?,?,?)")){
				pstmt.setInt(1,user.getId());
				pstmt.setString(2, user.getFirstName());
				pstmt.setString(3, user.getLastName());
				pstmt.setString(4, user.getEmail());
				pstmt.setString(5, user.getPassword());
				pstmt.setLong(6, user.getMobileno());
				pstmt.setString(7, user.getRole());
				int count = pstmt.executeUpdate();
				if(user.getEmail().isEmpty() && count==0) {
					return false;
				} else {
					return true;
				}
			}
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public User login(String email, String password) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from user where email=? and password=?");) {
				pstmt.setString(1,email);
				pstmt.setString(2,password);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					User bean = new User();
					bean.setId(rs.getInt("Id"));
					bean.setFirstName(rs.getString("firstName"));
					bean.setLastName(rs.getString("lastName"));
					bean.setEmail(rs.getString("email"));
					bean.setPassword(rs.getString("password"));
					bean.setMobileno(rs.getLong("mobile"));
					bean.setRole(rs.getString("role"));
					return bean;
				} else {
					return null;
				}
			}
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean addBook(Books book) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("insert into books values(?,?,?,?,?)");) {
				pstmt.setInt(1, book.getIsbn());
				pstmt.setString(2, book.getTitle());
				pstmt.setString(3, book.getAuthor());
				pstmt.setString(4, book.getCategory());
				pstmt.setString(5, book.getPublisher());
				//pstmt.setInt(6, book.getCopies());
				int count = pstmt.executeUpdate();
				if(count!=0) {
					return true;
				} else {
					return false;
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean removeBook(int isbn) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("delete from books where isbn=?");) {
				pstmt.setInt(1,isbn);
				int count=pstmt.executeUpdate();
				if(count!=0) {
					return true;
				} else {
					return false;
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateBook(Books book) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("update books set title=? where isbn=?");) {
				pstmt.setString(1,book.getTitle());
				pstmt.setInt(2,book.getIsbn());
				int count=pstmt.executeUpdate();
				if(count!=0) {
					return true;
				} else {
					return false;
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean issueBook(int isbn, int user_id) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from request_book where user_id=? and book_isbn=? and email=(select email from user where id=?)")) {
				pstmt.setInt(1, user_id);
				pstmt.setInt(2, isbn);
				pstmt.setInt(3, user_id);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					try(PreparedStatement pstmt1 = conn.prepareStatement("insert into issue_status values(?,?,?,?)");){
						pstmt1.setInt(1, isbn);
						pstmt1.setInt(2, user_id);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
						Calendar cal = Calendar.getInstance();
						String issueDate = sdf.format(cal.getTime());
						pstmt1.setDate(3, java.sql.Date.valueOf(issueDate));
						cal.add(Calendar.DAY_OF_MONTH, 7);
						String returnDate = sdf.format(cal.getTime());
						pstmt1.setDate(4, java.sql.Date.valueOf(returnDate));
						int count=pstmt1.executeUpdate();
						if(count != 0) {	
							try(PreparedStatement pstmt2 = conn.prepareStatement("Insert into borrow_book values(?,?,(select email from user where id=?))")){
								pstmt2.setInt(1, user_id);
								pstmt2.setInt(2, isbn);
								pstmt2.setInt(3, user_id);
								int isBorrowed = pstmt2.executeUpdate();
								if(isBorrowed != 0) {
									return true;
								}else {
									return false;
								}
							}
						} else {
							throw new LMSException("Book Not issued");
						}					
					}
				} else {
					throw new LMSException("The respective user have not placed any request");
				}			
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean request(int user_id, int isbn) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select count(*) as user_id from borrow_book where user_id=? and book_isbn=? and email=(select email from user where id=?)");) {
				pstmt.setInt(1, user_id);
				pstmt.setInt(2, isbn);
				pstmt.setInt(3, user_id);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					int isBookExists = rs.getInt("uId");
					if(isBookExists==0) {
						try(PreparedStatement pstmt1 = conn.prepareStatement("select count(*) as user_id from issue_status where user_id=?");) {
							pstmt1.setInt(1, user_id);
							rs=pstmt1.executeQuery();
							if(rs.next()) {
								int noOfBooksBorrowed = rs.getInt("user_id");
								if(noOfBooksBorrowed<3) {
									try(PreparedStatement pstmt2 = conn.prepareStatement("insert into request_book values(?,(select concat(firstname,'_',lastname) from user where id=?)"
											+ ",?,(select title from books where isbn=?),(select email from user where id=?))");){
										pstmt2.setInt(1,user_id);
										pstmt2.setInt(2, user_id);
										pstmt2.setInt(3, isbn);
										pstmt2.setInt(4, isbn);
										pstmt2.setInt(5, user_id);
										int count = pstmt2.executeUpdate();
										if(count != 0) {
											return true;
										}else {
											return false;
										}
									}				 
								}else {
									throw new LMSException("no Of books limit has crossed");
								}
							}else {
								throw new LMSException("no of books limit has crossed");
							}		
						}				
					}else{
						throw new LMSException("You have already borrowed the requested book");
					}		
				}else {
					throw new LMSException("You have already borrowed the requested book");
				}			
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public List<BorrowBooks> borrowedBook(int user_id) {

		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from borrow_book where user_id=?");) {
				pstmt.setInt(1, user_id);
				rs=pstmt.executeQuery();
				LinkedList<BorrowBooks> beans = new LinkedList<BorrowBooks>();
				while(rs.next()) {
					BorrowBooks listOfbooksBorrowed = new BorrowBooks();
					listOfbooksBorrowed.setUser_id(rs.getInt("user_id"));
					listOfbooksBorrowed.setBook_isbn(rs.getInt("book_isbn"));
					listOfbooksBorrowed.setEmail(rs.getString("email"));
					beans.add(listOfbooksBorrowed);
				} 
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<Books> searchBookById(int isbn) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from books where isbn=?");) {
				pstmt.setInt(1,isbn);
				rs=pstmt.executeQuery();
				LinkedList<Books> beans = new LinkedList<Books>();
				while(rs.next()) {
					Books bean = new Books();
					bean.setIsbn(rs.getInt("isbn"));
					bean.setTitle(rs.getString("title"));
					bean.setAuthor(rs.getString("author"));
					bean.setCategory(rs.getString("category"));
					bean.setPublisher(rs.getString("publisher"));
					//bean.setCopies(rs.getInt("copies"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<Books> searchBookByTitle(String title) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from books where title=?");) {
				pstmt.setString(1,title);
				rs=pstmt.executeQuery();
				LinkedList<Books> beans = new LinkedList<Books>();
				while(rs.next()) {
					Books bean = new Books();
					bean.setIsbn(rs.getInt("isbn"));
					bean.setTitle(rs.getString("title"));
					bean.setAuthor(rs.getString("author"));
					bean.setCategory(rs.getString("category"));
					bean.setPublisher(rs.getString("publisher"));
					//bean.setCopies(rs.getInt("copies"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<Books> searchBookByAuthor(String author) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from books where author=?");) {
				pstmt.setString(1,author);
				rs=pstmt.executeQuery();
				LinkedList<Books> beans = new LinkedList<Books>();
				while(rs.next()) {
					Books bean = new Books();
					bean.setIsbn(rs.getInt("isbn"));
					bean.setTitle(rs.getString("title"));
					bean.setAuthor(rs.getString("author"));
					bean.setCategory(rs.getString("category"));
					bean.setPublisher(rs.getString("publisher"));
					//bean.setCopies(rs.getInt("copies"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<Books> getBooksInfo() {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					Statement stmt = (Statement)conn.createStatement();) {
				rs = stmt.executeQuery("select * from books");
				LinkedList<Books> beans = new LinkedList<Books>();
				while(rs.next()) {
					Books bean = new Books();
					bean.setIsbn(rs.getInt("isbn"));
					bean.setTitle(rs.getString("title"));
					bean.setCategory(rs.getString("category"));
					bean.setAuthor(rs.getString("author"));
					bean.setPublisher(rs.getString("publisher"));
					//bean.setCopies(rs.getInt("copies"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean returnBook(int isbn, int user_id, String status) {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					PreparedStatement pstmt = conn.prepareStatement("select * from issue_status where book_isbn=? and user_id=?");) {
				pstmt.setInt(1, isbn);
				pstmt.setInt(2, user_id);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					Date issueDate = rs.getDate("issueDate");
					Date returnDate = rs.getDate("returnDate");
					long difference = issueDate.getTime() - returnDate.getTime();
					float daysBetween = (difference / (1000*60*60*24));
					if(daysBetween>7) {
						float fine = daysBetween*5;
						System.out.println("The user has to pay the fine of the respective book of Rs:"+fine);
						if(status=="yes") {
							try(PreparedStatement pstmt1 = conn.prepareStatement("delete from issue_status where book_isbn=? and user_id=?");) {
								pstmt1.setInt(1,isbn);
								pstmt1.setInt(2,user_id);
								int count =  pstmt1.executeUpdate();
								if(count != 0) {
									try(PreparedStatement pstmt2 = conn.prepareStatement("delete from borrow_book where book_isbn=? and user_id=?");) {
										pstmt2.setInt(1, isbn);
										pstmt2.setInt(2, user_id);
										int isReturned = pstmt2.executeUpdate();
										if(isReturned != 0 ) {
											try(PreparedStatement pstmt3 = conn.prepareStatement("delete from request_book where book_isbn=? and user_id=?");){
												pstmt3.setInt(1, isbn);
												pstmt3.setInt(2, user_id);
												int isRequestDeleted = pstmt3.executeUpdate();
												if(isRequestDeleted != 0) {
													return true;
												}else {
													return false;
												}
											}
										}else {
											return false;
										}
									}
								} else {
									return false;
								}
							}
						} else {
							throw new LMSException("The User has to pay fine for delaying book return");
						}
					}else {
						try(PreparedStatement pstmt1 = conn.prepareStatement("delete from issue_status where book_isbn=? and user_id=?");) {
							pstmt1.setInt(1,isbn);
							pstmt1.setInt(2,user_id);
							int count =  pstmt1.executeUpdate();
							if(count != 0) {
								try(PreparedStatement pstmt2 = conn.prepareStatement("delete from borrow_book where book_isbn=? and user_id=?");) {
									pstmt2.setInt(1, isbn);
									pstmt2.setInt(2, user_id);
									int isReturned = pstmt2.executeUpdate();
									if(isReturned != 0 ) {
										try(PreparedStatement pstmt3 = conn.prepareStatement("delete from request_book where book_isbn=? and user_id=?");){
											pstmt3.setInt(1, isbn);
											pstmt3.setInt(2, user_id);
											int isRequestDeleted = pstmt3.executeUpdate();
											if(isRequestDeleted != 0) {
												return true;
											}else {
												return false;
											}
										}
									}else {
										return false;
									}
								}
							} else {
								return false;
							}
						}
					}
				}else {
					throw new LMSException("This respective user hasn't borrowed any book");
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public LinkedList<RequestBook> showRequests() {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					Statement stmt = (Statement)conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from request_book");) {
				LinkedList<RequestBook> beans = new LinkedList<RequestBook>();
				while(rs.next()) {
					RequestBook bean = new RequestBook();
					bean.setUser_id(rs.getInt("user_id"));
					bean.setFullName(rs.getString("fullName"));
					bean.setBook_isbn(rs.getInt("book_isbn"));
					bean.setTitle(rs.getString("title"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<IssueStatus> showIssuedBooks() {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					Statement stmt = (Statement)conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from issue_status");) {
				LinkedList<IssueStatus> beans = new LinkedList<IssueStatus >();
				while(rs.next()) {
					IssueStatus bean = new IssueStatus();
					bean.setBook_isbn(rs.getInt("Book_isbn"));
					bean.setUser_id(rs.getInt("user_id"));
					bean.setIssuedDate(rs.getDate("issuedDate"));
					bean.setReturnedDate(rs.getDate("returnedDate"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public LinkedList<User> showUsers() {
		try(FileInputStream info = new FileInputStream("db.properties");){
			Properties pro = new Properties();
			pro.load(info);
			Class.forName(pro.getProperty("path"));
			try(Connection conn = DriverManager.getConnection(pro.getProperty("dburl"),pro);
					Statement stmt = (Statement)conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from user");) {
				LinkedList<User> beans = new LinkedList<User>();
				while(rs.next()) {
					User bean = new User();
					bean.setId(rs.getInt("id"));
					bean.setFirstName(rs.getString("firstName"));
					bean.setLastName(rs.getString("lastName"));
					bean.setMobileno(rs.getLong("mobileno"));
					bean.setRole(rs.getString("role"));
					bean.setEmail(rs.getString("email"));
					bean.setPassword(rs.getString("password"));
					beans.add(bean);
				}
				return beans;
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}


}
