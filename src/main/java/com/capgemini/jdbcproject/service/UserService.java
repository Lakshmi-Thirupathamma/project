package com.capgemini.jdbcproject.service;

import java.util.LinkedList;
import java.util.List;

import com.capgemini.jdbcproject.dto.Books;
import com.capgemini.jdbcproject.dto.BorrowBooks;
import com.capgemini.jdbcproject.dto.IssueStatus;
import com.capgemini.jdbcproject.dto.RequestBook;
import com.capgemini.jdbcproject.dto.User;

public interface UserService {
	
	boolean register(User user);
	User login(String email,String password);
	boolean addBook(Books book);
	boolean removeBook(int isbn);
	boolean updateBook(Books book);
	boolean issueBook(int isbn,int user_id);
	boolean request(int user_id, int isbn);
	List<BorrowBooks> borrowedBook(int user_id);
	LinkedList<Books> searchBookById(int isbn);
	LinkedList<Books> searchBookByTitle(String title);
	LinkedList<Books> searchBookByAuthor(String author);
	LinkedList<Books> getBooksInfo();
	boolean returnBook(int isbn,int user_id,String status);
	LinkedList<RequestBook> showRequests();
	LinkedList<IssueStatus> showIssuedBooks();
	LinkedList<User> showUsers();

}
