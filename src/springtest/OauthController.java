package springtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpSession;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.simple.JSONObject;


import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
//import com.google.appengine.repackaged.com.google.gson.Gson;

@Controller  
@RequestMapping("/authen")
public class OauthController {
	@RequestMapping("/check")  
    public ModelAndView authen() {  
      
		return new ModelAndView("redirect:https://accounts.google.com/o/oauth2/auth?redirect_uri=http://localhost:8888/authen/get&response_type=code&client_id=21895785036-j4gre0d0thmqn9e3ac13ab5bq4l5akt5.apps.googleusercontent.com&approval_prompt=force&scope=email&access_type=online");
    
	}  
	
	@RequestMapping("/add")  
    public ModelAndView gotoAddpage() {  
      
    	return new ModelAndView("add");  
    
    }
	
	@RequestMapping("/get")  
    public ModelAndView getValue(@RequestParam("code") String note,HttpSession session) throws IOException {  
	
			String clientId="21895785036-j4gre0d0thmqn9e3ac13ab5bq4l5akt5.apps.googleusercontent.com";
			String clientSecret="XbW_r8rTTQRvR0nzbTaZfSYJ";
			String redirect_url="http://localhost:8888/authen/get";
			System.out.println(""+note);
			String grant_type="authorization_code";
			URL obj = new URL("https://www.googleapis.com/oauth2/v3/token?client_id="+clientId+"&client_secret=" + clientSecret+ "&redirect_uri=" + redirect_url + "&grant_type="+ grant_type + "&code=" + note);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		    con.setDoOutput(true);
	    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			String response = "";
			while ((inputLine = in.readLine()) != null) {
				response+=inputLine;
			}
			in.close();
			System.out.println(response.toString());
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = null;
			try {
				jsonObject = (JSONObject) parser.parse(response);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
			String access_token = (String) jsonObject.get("access_token");
			System.out.println("got it ="+access_token);
			URL obj1 = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+access_token);
			HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		    BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine1;
			String responsee = "";
			while ((inputLine1 = in1.readLine()) != null) {
				responsee+=inputLine1;
			}
			in1.close();
			System.out.println(responsee.toString());
			JSONObject jsonObject2 = null;
			try {
				jsonObject2 = (JSONObject) parser.parse(responsee);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
			String email = (String) jsonObject2.get("email");
		    String name=(String)  jsonObject2.get("name");
		    String id=(String)  jsonObject2.get("id");
		    
		    session.setAttribute("name", jsonObject2.get("name"));
			session.setAttribute("email", jsonObject2.get("email"));
			session.setAttribute("picture", jsonObject2.get("picture"));
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			List<User> results = null;
			Query q = pm.newQuery(User.class);
			q.setFilter("email =='"+email+"'");
			try {
				results = (List<User>) q.execute();
				if (results.isEmpty()) {
					User user = new User();
					user.setName(name);
					user.setEmail(email);
					user.setId(id);
					pm.makePersistent(user);
				}
				else{
					System.out.println("User exist already:");
				}
					
			} finally {
				q.closeAll();
				pm.close();
			}
			
			ModelAndView view=new ModelAndView("redirect:/authen/first");
			return view;	
	//return "send";
	}

	@RequestMapping("first")  
	public String toHellopage(ModelMap model,HttpSession session){
	
		String email = (String) session.getAttribute("email");
		if(session.getAttribute("email")==null || session.getAttribute("name")==null){
			return "redirect:../";
		}else{
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
	    Iterator iterator = result.iterator();
	    while (iterator.hasNext())
	    {
	    	Todo check=new Todo();
	    	check = (Todo)iterator.next();
	         System.out.println(check.getContent());
	    }
	    System.out.println(result);
			pm.close();
		
		
		return "hellopage";
		}
		//return "hellopage";
		}
	
	@RequestMapping("yest")  
	public String toyesterdaypage(ModelMap model,HttpSession session){
	
		String email = (String) session.getAttribute("email");
		if(session.getAttribute("email")==null || session.getAttribute("name")==null){
			return "redirect:../";
		}else{
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
	    Iterator iterator = result.iterator();
	    while (iterator.hasNext())
	    {
	    	Todo check=new Todo();
	    	check = (Todo)iterator.next();
	         System.out.println(check.getContent());
	    }
	    System.out.println(result);
			pm.close();
		
		
		return "yesterday";
		}
		//return "hellopage";
		}
	
	@RequestMapping("week")  
	public String toLastOneWeekpage(ModelMap model,HttpSession session){
	
		String email = (String) session.getAttribute("email");
		if(session.getAttribute("email")==null || session.getAttribute("name")==null){
			return "redirect:../";
		}else{
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
	    Iterator iterator = result.iterator();
	    while (iterator.hasNext())
	    {
	    	Todo check=new Todo();
	    	check = (Todo)iterator.next();
	         System.out.println(check.getContent());
	    }
	    System.out.println(result);
			pm.close();
		
		
		return "lastoneweek";
		}
		//return "hellopage";
		}

	
	
	@RequestMapping(value="/ajax", method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String ajaxcall(@RequestBody String todo,HttpSession session){
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) parser.parse(todo);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		String content = (String) jsonObject.get("content");
		Todo obj=new Todo();
		obj.setContent(content);
		obj.setDate(new Date());
		String email = (String) session.getAttribute("email");
		String name = (String) session.getAttribute("name");
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
	    currentUser.addTodo(obj);
		
	    try {
			pm.makePersistent(currentUser);
		} finally {
			pm.close();
		}
	    
	    //*****************mail properties***************
	    Properties prop = new Properties();
	    Session sessio = Session.getDefaultInstance(prop,null);
	    try{    
	        Message mimeMessage = new MimeMessage(sessio);
	        mimeMessage.setFrom(new InternetAddress("sriram.umapathy@a-cti.com"));
	        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "Mr./Ms. "+name));
	        mimeMessage.setSubject("ToDo List successfully added note");
	        mimeMessage.setText("Name :: "+name+"\nEmail-id :: "+email+"\n\nYour note as been added sussessfully ::\n"+obj.getContent());
	        Transport.send(mimeMessage);
	        System.out.println("Successfull Delivery.");
	    } catch (AddressException e) {
	        e.printStackTrace();
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    //**********end**************************
	    Gson gson = new Gson();
        //convert java object to JSON format,
		//and returned as JSON formatted string
		String json = gson.toJson(obj);
		//System.out.println(json);
		System.out.println("got the json value ==="+json);
	    
		System.out.println("got it ="+content+" by "+email);
	
		System.out.println("came in"+todo);
		String a="came";
		return json;
		}
		@RequestMapping(value="deletedate/{id}",method = RequestMethod.GET)	
		public @ResponseBody String deleteAjaxList(@PathVariable Key id,HttpSession session) throws ParseException{
    		String email = (String) session.getAttribute("email");
    		System.out.println("got in to this method"+id);
    		
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		try {
    			Todo todo = pm.getObjectById(Todo.class,id);
    			pm.deletePersistent(todo);
    			System.out.println("success deleted");
    	    } finally {
    			pm.close();
    		}
    		
    		/*String pattern = "MMM d, Y hh:mm:ss a";
    	    SimpleDateFormat format = new SimpleDateFormat(pattern);
    	    //SimpleDateFormat format2 = new SimpleDateFormat(pattern);
    	    // Date datee=new Date( Oct 10, 2015 11:57:18 AM );
    	    //Date dateobj = format.parse(date);
    	   // System.out.println("this is date obj"+dateobj);
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
    	    currentUser.deleteTodoWithKey(KeyFactory.keyToString(date));

    	    try {
    			pm.makePersistent(currentUser);
    		} finally {
    			pm.close();
    		}
    	    */
    		Gson gson = new Gson();
            //convert java object to JSON format,
    		//and returned as JSON formatted string
    		String json = gson.toJson(id);
    	    return json;
    
    	}	
	
		}
