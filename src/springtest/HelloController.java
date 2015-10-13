package springtest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;  
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;  
import java.util.Properties;
import java.io.UnsupportedEncodingException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;


@Controller  
//@RequestMapping("")
public class HelloController {  
    @RequestMapping("/")  
    public ModelAndView helloWorld() {  
      
    	return new ModelAndView("login");  
    
    }
    @RequestMapping("/logout")  
    public ModelAndView logout(HttpSession session) {  
      
    	if(session != null){
			session.invalidate();
			
		}
    	return new ModelAndView("login");  
    
    }
    
    
    @RequestMapping(value="add",method = RequestMethod.GET)	
    public ModelAndView addList(@RequestParam String data,ModelMap model,HttpSession session){
    
    String email = (String) session.getAttribute("email");
    String name = (String) session.getAttribute("name");
    Todo obj=new Todo();
	obj.setContent(data);
	obj.setDate(new Date());
	
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Query q = pm.newQuery(User.class, "email == value");
	q.declareParameters("String value");
    List<User> results = (List<User>) q.execute(email);
    Iterator iter = results.iterator();
    String a=results.toString();
    User currentUser = null;
    while (iter.hasNext())
    {
         currentUser = (User)iter.next();
    }
    currentUser.addTodo(obj);
	
    try {
		pm.makePersistent(currentUser);
	} finally {
		pm.close();
	}
    
    Properties prop = new Properties();
    Session sessio = Session.getDefaultInstance(prop,null);
    try{    
        Message mimeMessage = new MimeMessage(sessio);
        mimeMessage.setFrom(new InternetAddress("palanisriram57@gmail.com"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "Mr./Ms. "+name));
        mimeMessage.setSubject("Message from BMI calculations app");
        mimeMessage.setText("Name :: "+name+"\nEmail-id :: "+email+"\n\nMessage body ::\n"+email);
        Transport.send(mimeMessage);
        System.out.println("Successfull Delivery.");
    } catch (AddressException e) {
        e.printStackTrace();
    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    
    
    ModelAndView view=new ModelAndView("redirect:/first");
	return view;
    		
    }
    
    @RequestMapping("first")  
    public String toHellopage(ModelMap model,HttpSession session){
    	
    String email = (String) session.getAttribute("email");
    		
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		Query q = pm.newQuery(User.class, "email == value");
    		q.declareParameters("String value");
    	    List<User> results = (List<User>) q.execute(email);
    	    Iterator iter = results.iterator();
    	    User currentUser = null;
    	    while (iter.hasNext())
    	    {
    	         currentUser = (User)iter.next();
    	    }
    	   
    	    List<Todo> result= (List<Todo>)currentUser.getUserTodo();
    	    if (result.isEmpty()) {
    			model.addAttribute("todoList", null);
    		} else {
    			model.addAttribute("todoList", result);
    		}
    	    System.out.println(result);
    			pm.close();
    		
    		
    	return "hellopage";
    }
    
    @RequestMapping(value="delete/{key}",method = RequestMethod.GET)	
    public ModelAndView deleteList(@PathVariable String key,ModelMap model,HttpSession session){
    String email = (String) session.getAttribute("email");
    		System.out.println(key);
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		Query q = pm.newQuery(User.class, "email == value");
    		q.declareParameters("String value");
    	    List<User> results = (List<User>) q.execute(email);
    	    Iterator iter = results.iterator();
    	    User currentUser = null;
    	    while (iter.hasNext())
    	    {
    	         currentUser = (User)iter.next();
    	    }
    	    currentUser.deleteTodoWithKey(key);

    	    try {
    			pm.makePersistent(currentUser);
    		} finally {
    			pm.close();
    		}
    	
    	    ModelAndView view=new ModelAndView("redirect:/authen/first");
			return view;
    
    	}	
    
   
    @RequestMapping(value="edit/{key}",method = RequestMethod.GET)	
    public String editList(@PathVariable String key,ModelMap model){
    	
    	PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Todo todo = pm.getObjectById(Todo.class,key);
			model.addAttribute("todo", todo);
	    } finally {
			pm.close();
		}
		return "edit";
    	
    }
    @RequestMapping(value="editvalue",method = RequestMethod.POST)	
    public ModelAndView editedList(@RequestParam String key,@RequestParam String content,ModelMap model){
    	
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	try {
			Todo todo = pm.getObjectById(Todo.class,key);
			todo.setContent(content);
			}finally {
				pm.close();
			}
    	 ModelAndView view=new ModelAndView("redirect:/authen/first");
			return view;
 
    }
}  




	
	

