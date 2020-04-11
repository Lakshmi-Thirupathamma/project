package com.capgemini.jdbcproject.service;

import java.util.LinkedList;
import java.util.List;

import com.capgemini.jdbcproject.dao.UserDao;
import com.capgemini.jdbcproject.dto.Books;
import com.capgemini.jdbcproject.dto.BorrowBooks;
import com.capgemini.jdbcproject.dto.IssueStatus;
import com.capgemini.jdbcproject.dto.RequestBook;
import com.capgemini.jdbcproject.dto.User;
import com.capgemini.jdbcproject.factory.LMSFactory;

public class UserServiceImp implements UserService {
	private UserDao dao = LMSFactory.getUserDAO();

	@Override
	public boolean register(User user) {
		return dao.register(user);
	}

	@Override
	public User login(String email, String password) {
		return dao.login(email, password);
	}

	@Override
	public boolean addBook(Books book) {
		return dao.addBook(book);
	}

	@Override
	public boolean removeBook(int isbn) {
		return dao.removeBook(isbn);
	}

	@Override
	public boolean updateBook(Books book) {
		return dao.updateBook(book);
	}

	@Override
	public boolean issueBook(int isbn, int user_id) {
		return dao.issueBook(isbn, user_id);
	}

	@Override
	public boolean request(int user_id, int isbn) {
		return dao.request(user_id, isbn);
	}

	@Override
	public List<BorrowBooks> borrowedBook(int user_id) {
		return dao.borrowedBook(user_id);
	}

	@Override
	public LinkedList<Books> searchBookById(int isbn) {
		return dao.searchBookById(isbn);
	}

	@Override
	public LinkedList<Books> searchBookByTitle(String title) {
		return dao.searchBookByTitle(title);
	}

	@Override
	public LinkedList<Books> searchBookByAuthor(String author) {
		return dao.searchBookByAuthor(author);
	}

	@Override
	public LinkedList<Books> getBooksInfo() {
		return dao.getBooksInfo();
	}

	@Override
	public boolean returnBook(int isbn, int user_id, String status) {
		return dao.returnBook(isbn, user_id, status);
	}

	@Override
	public LinkedList<RequestBook> showRequests() {
		return dao.showRequests();
	}

	@Override
	public LinkedList<IssueStatus> showIssuedBooks() {
		return dao.showIssuedBooks();
	}

	@Override
	public LinkedList<User> showUsers() {
		return dao.showUsers();
	}


}
