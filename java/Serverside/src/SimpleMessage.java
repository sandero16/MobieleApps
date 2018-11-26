import org.json.simple.JSONObject;
import static java.lang.Math.toIntExact;

public class SimpleMessage {

	int type;
	/* 0 --> login/logout
	 * 1 --> add message to conversation
	 */
	
	String message;
	@Override
	public String toString() {
		return "SimpleMessage [type=" + type + ", message=" + message + ", extra=" + extra + "]";
	}

	String extra;
	
	public SimpleMessage(int type, String message, String extra) {
		
		this.type=type;
		this.message = message;
		this.extra =extra;
		
	}
	
	//create from JSON
	public SimpleMessage(JSONObject jo) {
		
		type = toIntExact((Long) jo.get("type"));
		message = (String) jo.get("message");
		extra = (String) jo.get("extra");
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	//convert to JSON
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();

	      obj.put("type", new Integer(type));
	      obj.put("message", message);
	      obj.put("extra", extra);
	      
	      return obj;
		
	}

	public String getExtra() {
		// TODO Auto-generated method stub
		return extra;
	}
	
	
}
