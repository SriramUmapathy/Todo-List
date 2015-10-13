package springtest;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
//import javax.jdo.annotations.Key;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Todo {

	 @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private Key key;
		
	    @Persistent
	    private String content;

		@Persistent
	    private Date date;
	    
	    @Persistent
	    private Boolean done;

		public Boolean getDone() {
			return done;
		}

		public void setDone(Boolean done) {
			this.done = done;
		}

		public String getKey() {
			return KeyFactory.keyToString(key);
		}

		public void setKey(Key key) {
			this.key = key;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		
}
