package springtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Key;

@PersistenceCapable
public class User {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String id;
	
	@Persistent
    private String email;
	@Persistent
    private String name;
	
	@Persistent
    private List<Todo> todo = new ArrayList<Todo>();
	
	public List<Todo> getUserTodo() {
		return todo;
	}
	public void setUserNotes(List<Todo> todo) {
		this.todo = todo;
	}
	
	//Append new note to exisiting note
	public void addTodo(Todo todo){
		this.todo.add(todo);
	}
	
	 
	public void deleteTodoWithKey(String key) {
		Iterator<Todo> iterator = this.todo.iterator();
		while(iterator.hasNext()){
			Todo todo = (Todo) iterator.next();
			//System.out.println("ID "+todo.getID());
			if(todo.getKey().equals(key)){
				iterator.remove();
			}
			
		}
	}
	public void deleteTodoWithdate(Date key) {
		Iterator<Todo> iterator = this.todo.iterator();
		System.out.println("inside the user class");
		while(iterator.hasNext()){
			Todo todo = (Todo) iterator.next();
			if(todo.getDate().equals(key)){
				iterator.remove();
			}
			
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
