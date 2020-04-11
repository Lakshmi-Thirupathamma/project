package com.capgemini.jdbcproject.controller;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.capgemini.jdbcproject.dto.Books;
import com.capgemini.jdbcproject.dto.BorrowBooks;
import com.capgemini.jdbcproject.dto.IssueStatus;
import com.capgemini.jdbcproject.dto.RequestBook;
import com.capgemini.jdbcproject.dto.User;
import com.capgemini.jdbcproject.exception.LMSException;
import com.capgemini.jdbcproject.factory.LMSFactory;
import com.capgemini.jdbcproject.service.UserService;
import com.capgemini.jdbcproject.validation.LMSValidation;

public class Controller {

	public static void main(String[] args) {
		doReg();
	}
	public static void doReg() {
		boolean flag = false;

		int regId = 0; 
		String regFirstName = null; 
		String regLastName = null; 
		long regMobile = 0;
		String regEmail = null;
		String regPassword = null;

		boolean loginStatus = true;
		LMSValidation validation = new LMSValidation();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press 1 to Register");
		System.out.println("Press 2 to Login");
		System.out.println("Press 3 to EXIT");
		do {
			UserService service1= LMSFactory.getUserService();
			int choice = scanner.nextInt();
			switch(choice) {
			case 1:
				do {
					try {
						System.out.println("Enter ID :");
						regId = scanner.nextInt();
						validation.validateId(regId);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Id should contains only digits");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);

				do {
					try {
						System.out.println("Enter First Name :");
						regFirstName = scanner.next();
						validation.validateName(regFirstName);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Name should contains only Alphabates");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);
				do {
					try {
						System.out.println("Enter Last Name :");
						regLastName = scanner.next();
						validation.validateName(regLastName);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Name should contains only Alphabates");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);

				

				do {
					try {
						System.out.println("Enter Mobile :");
						regMobile = scanner.nextLong();
						validation.validateMobile(regMobile);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Mobile Number  should contains only numbers");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);
				System.out.println("Enter the role");
				String regRole = scanner.next();
				do {
					try {
						System.out.println("Enter Email :");
						regEmail = scanner.next();
						validation.validateEmailId(regEmail);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Email should be proper ");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);

				do {
					try {
						System.out.println("Enter Password :");
						regPassword = scanner.next();
						validation.validatePassword(regPassword);
						flag = true;
					} catch (InputMismatchException e) {
						flag = false;
						System.err.println("Enter correct Password ");
					} catch (LMSException e) {
						flag = false;
						System.err.println(e.getMessage());
					}
				} while (!flag);
				User ai = new User();
				ai.setId(regId);
				ai.setFirstName(regFirstName);
				ai.setLastName(regLastName);
				ai.setMobileno(regMobile);
				ai.setRole(regRole);
				ai.setEmail(regEmail);
				ai.setPassword(regPassword);
				try {
					boolean check=service1.register(ai);
					if(check) {
						System.out.println("Registered");
					}else {
						System.out.println("Already user is registered");
					}
				}catch (LMSException e) {
					System.err.println(e.getMessage());
				}
				break;
			case 2:
				System.out.println("enter email");
				String email=scanner.next();
				System.out.println("enter password");
				String password=scanner.next();
				try {
					User loginInfo=service1.login(email, password);
					if(loginInfo.getEmail().equals(email) && loginInfo.getPassword().equals(password)) {
						System.out.println("Logged In");
					}
					if(loginInfo.getRole().equals("admin")) {
						do {
							System.out.println("-----------------------------------------------");
							System.out.println("Press 1 to add book");
							System.out.println("Press 2 to remove book");
							System.out.println("Press 3 to issue book");
							System.out.println("Press 4 to Search the Book by Author");
							System.out.println("Press 5 to Search the Book by Title");
							System.out.println("Press 6 to Get the Book Information");
							System.out.println("Press 7 to Search the book by Id");
							System.out.println("Press 8 to update the book");
							System.out.println("Press 9 to check student book history");
							System.out.println("Press 10 to check requests");
							System.out.println("Press 11 to check issued books");
							System.out.println("Press 12 to view users");
							System.out.println("Press 13 to logout");

							int choice1 = scanner.nextInt();
							switch (choice1) {
							case 1:
								System.out.println("enter isbn");
								int addIsbn=scanner.nextInt();
								System.out.println("enter title");
								String addName=scanner.next();
								System.out.println("enter category");
								String addCategory=scanner.next();
								System.out.println("enter authorname");
								String addAuth=scanner.next();
								System.out.println("enter publisher");
								String addPublisher=scanner.next();
								/*
								 * System.out.println("enter no of copies"); int addCopies = scanner.nextInt();
								 */
								Books bi =new Books();
								bi.setIsbn(addIsbn);
								bi.setTitle(addName);
								bi.setCategory(addCategory);
								bi.setAuthor(addAuth);
								bi.setPublisher(addPublisher);
								//bi.setCopies(addCopies);
								try {
									boolean check2=service1.addBook(bi);
									if(check2) {
										System.out.println("-----------------------------------------------");
										System.out.println("Added Book");
									}else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book not added");
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}

								break;	
							case 2:
								System.out.println("enter isbn");
								int removeId=scanner.nextInt();
								try {
									boolean check3=service1.removeBook(removeId);
									if(check3) {
										System.out.println("-----------------------------------------------");
										System.out.println("Removed Book");
									}else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book not removed");
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 3:
								System.out.println("enter Book Isbn");
								int issueId=scanner.nextInt();
								System.out.println("Enter User Id");
								int userId = scanner.nextInt();
								try {
									boolean check4=service1.issueBook(issueId,userId);
									if(check4) {
										System.out.println("-----------------------------------------------");
										System.out.println("Book Issued");
									}else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book not issued");
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 4:
								System.out.println("Search the book by the Author Name:");
								String author = scanner.next();
								try {
									List<Books> bookauthor = service1.searchBookByAuthor(author);
									for (Books bookBean : bookauthor) {

										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available written by this author");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 5:
								System.out.println("  Search the book by the Book_Title :");
								String btitle = scanner.next();
								try {
									List<Books> booktitle = service1.searchBookByTitle(btitle);
									for (Books bookBean : booktitle) {	
										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available with this title.");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 6:
								try {
									LinkedList<Books> info = service1.getBooksInfo();
									for (Books bookBean : info) {

										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("Books info is not present");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 7:
								System.out.println("  Search the book by the Book_ID :");
								int book_Id = scanner.nextInt();
								try {
									List<Books> bId = service1.searchBookById(book_Id);
									for (Books bookBean : bId) {	
										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available with this ID.");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 8:
								System.out.println("Enter the updated id :");
								int bid= scanner.nextInt();
								System.out.println("Enter bookName to be updtaed");
								String updatedBookName =scanner.next();
								Books bean2 = new Books();
								bean2.setIsbn(bid);
								bean2.setTitle(updatedBookName);
								try {
									boolean updated = service1.updateBook(bean2);
									if (updated) {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is updated");
									} else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is not updated");
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 10:
								System.out.println(" Requests received are:");
								try {
									List<RequestBook> requests = service1.showRequests();
									for (RequestBook requestBean : requests) {	
										if (requestBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("User_Id is-->"+requestBean.getUser_id());
											System.out.println("User_Name is-->"+requestBean.getFullName());
											System.out.println("Book_Id is-->"+requestBean.getBook_isbn());
											System.out.println("BookName is-->"+requestBean.getTitle());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No Requests are received");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 11:
								System.out.println("Issued Books are:");
								try {
									List<IssueStatus> issuedBooks = service1.showIssuedBooks();
									for (IssueStatus issueBean : issuedBooks) {	
										if (issueBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+issueBean.getBook_isbn());
											System.out.println("User_Id is-->"+issueBean.getUser_id());
											System.out.println("Issue_Date is-->"+issueBean.getIssuedDate());
											System.out.println("Return_Date is-->"+issueBean.getReturnedDate());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No book has been issued");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 12:
								System.out.println("Users are:");
								try {
									List<User> users = service1.showUsers();
									for (User Bean : users) {	
										if (Bean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("User_Id is-->"+Bean.getId());
											System.out.println("First_Name is-->"+Bean.getFirstName());
											System.out.println("Last_Name is-->"+Bean.getLastName());
											System.out.println("Mobile_No is-->"+Bean.getMobileno());
											System.out.println("User's_Role is-->"+Bean.getRole());
											System.out.println("Email_Id is-->"+Bean.getEmail());
											System.out.println("Password is-->"+Bean.getPassword());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No Users are present");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;


							case 13:
								doReg();

							default:
								System.out.println("-----------------------------------------------");
								System.out.println("Invalid Choice");
								break;
							}
						}while(true);
					}
					else if(loginInfo.getRole().equals("student")) {
						do {
							System.out.println("-----------------------------------------------");
							System.out.println("Press 1 to request book");
							System.out.println("Press 2 to view the books borrowed");
							System.out.println("Press 3 to search book by author");
							System.out.println("Press 4 to search book by title");
							System.out.println("Press 5 to search book by Id");
							System.out.println("Press 6 to get books info");
							System.out.println("Press 7 to return book");
							System.out.println("Press 8 to main");


							int choice2 = scanner.nextInt();
							switch (choice2) {
							case 1:
								System.out.println("Enter the Book Id:");
								int reqBookId= scanner.nextInt();
								System.out.println("Enter the user Id:");
								int reqUserId = scanner.nextInt();
								try {
									boolean requested = service1.request(reqUserId,reqBookId);
									if (requested != false) {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is Requested");
									} else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is not Requested");
									}	
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 2:
								System.out.println("Enter the user Id");
								int user_Id = scanner.nextInt();
								try {
									List<BorrowBooks> borrowedBookList = service1.borrowedBook(user_Id);
									for (BorrowBooks bookBean : borrowedBookList) {

										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("User_Id is-->"+bookBean.getUser_id());
											System.out.println("Book_Id is-->"+bookBean.getBook_isbn());
											System.out.println("Email Id is-->"+bookBean.getEmail());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are borrowed by the user");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 3:
								System.out.println("Search the book by the Author Name :");
								String author = scanner.next();
								try {
									List<Books> bookauthor = service1.searchBookByAuthor(author);
									for (Books bookBean : bookauthor) {

										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available written by this author");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 4:
								System.out.println("Search the book by the Book Title :");
								String btitle = scanner.next();

								try {
									List<Books> booktitle = service1.searchBookByTitle(btitle);
									for (Books bookBean : booktitle) {	
										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available with this title.");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 5:
								System.out.println("  Search the book by the Book_ID :");
								int book_Id = scanner.nextInt();

								try {
									List<Books> bId = service1.searchBookById(book_Id);
									for (Books bookBean : bId) {	
										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("No books are available with this title.");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 6:
								try {
									LinkedList<Books> info = service1.getBooksInfo();
									for (Books bookBean : info) {

										if (bookBean != null) {
											System.out.println("-----------------------------------------------");
											System.out.println("Book_Id is-->"+bookBean.getIsbn());
											System.out.println("Book_Name is-->"+bookBean.getTitle());
											System.out.println("Book_Category is-->"+bookBean.getCategory());
											System.out.println("Book_Author is-->"+bookBean.getAuthor());
											System.out.println("Book_PublisherName is-->"+bookBean.getPublisher());
											
											//System.out.println("No_Of_Copies are-->"+bookBean.getCopies());
										} else {
											System.out.println("-----------------------------------------------");
											System.out.println("Books info is not presernt");
										}
									}
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;
							case 7:
								System.out.println("Enter the Book id to return :");
								int returnId= scanner.nextInt();
								System.out.println("Enter userId");
								int userId = scanner.nextInt();	
								System.out.println("Enter the status of the book");
								String status = scanner.next();
								try {
									boolean returned = service1.returnBook(returnId,userId,status);
									if (returned != false) {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is Returned");
									} else {
										System.out.println("-----------------------------------------------");
										System.out.println("Book is not Returned");
									}	
								}catch (LMSException e) {
									System.err.println(e.getMessage());
								}
								break;

							case 8:
								doReg();
							
							default:
								break;
							}
						}while(true);
					}
				}catch(Exception e) {
					System.out.println("Invalid Credentials");
					System.out.println("Try logging in again,Press 2 to login");
				}
				break;
			case 3:
				System.out.println("Exit");
				loginStatus = false;
			}
		}while(loginStatus);
	}
}