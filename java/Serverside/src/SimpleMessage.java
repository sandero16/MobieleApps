import org.json.simple.JSONObject;

public class SimpleMessage {

	int type;
	/* 0 --> login/logout
	 * 1 --> add message to conversation
	 * 2 --> add user to conversation
	 * 3 --> start new conversation
	 */
	
	String message;
	
	public SimpleMessage(int type, String message) {
		
		this.type=type;
		this.message = message;
		
	}
	
	//create from JSON
	public SimpleMessage(JSONObject jo) {
		
		type = (Integer) jo.get("type");
		message = (String) jo.get("message");
		
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
	      
	      return obj;
		
	}
	
	
}
