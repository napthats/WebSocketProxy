package com.napthats.websocket;

import java.util.ArrayList;
import java.util.Properties;

/**
 * extract SpecialCommand rules from property file 
 * @author napthats
 * 
 */
public class SpecialCommandSet {
	public static enum Status {ACTIVE, NONACTIVE};
	public static enum Func {CONNECT, DISCONNECT};
	private ArrayList<SpecialCommand> scSet;
	private static final String MESSAGE_INVALID_SPECIALCOMMAND = "invalid properties(invalid SpecialCommand)";
	private static final String SEPARATOR_COMMAND = ",";
	private static final String SEPARATOR_ELEMENT = ":";
	private static final String PROPERTY_CONNECT = "Connect";
	private static final String PROPERTY_DISCONNECT = "Disconnect";	
	private static final String PROPERTY_SPECIAL_COMMAND = "SpecialCommand";
	
	//use "SpecialCommand" if it exists
	//else use "Connect" and "Disconnect"
	public SpecialCommandSet(Properties prop) {
		String scStringSet = prop.getProperty(PROPERTY_SPECIAL_COMMAND);
	    scSet = new ArrayList<SpecialCommandSet.SpecialCommand>();
		if (scStringSet != null) {
		    for (String scString : scStringSet.split(SEPARATOR_COMMAND)) {
			    scSet.add(new SpecialCommand(scString));
		    }
		}
		else {
			String connectStringSet = prop.getProperty(PROPERTY_CONNECT);
			String disconnectStringSet = prop.getProperty(PROPERTY_DISCONNECT);
			if (connectStringSet == null || disconnectStringSet == null) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);

			for (String disconnectString: disconnectStringSet.split(SEPARATOR_COMMAND)) {
				scSet.add(new SpecialCommand(Func.DISCONNECT, disconnectString));
			}
			for (String connectString: connectStringSet.split(SEPARATOR_COMMAND)) {
				scSet.add(new SpecialCommand(Func.CONNECT, connectString));
			}
		}
	}
	
	//search SpecialCommand with current Status and user's statement 
	public SpecialCommand findSpecialCommand(Status targetStatus, String targetString) {
		for (SpecialCommand sc : scSet) {
			if (sc.commandWord.equals(targetString) && sc.relatedStatus == targetStatus) return sc; 
		}
		return null;
	}
	
	public class SpecialCommand {
		private String commandWord;
		private Status relatedStatus;
		private Func invokeMethod;
		private ArrayList<String> optionList;
		
		//with specified Func
		private SpecialCommand(Func _invokeMethod, String scString) {
			if (_invokeMethod == Func.DISCONNECT) {
				commandWord = scString;
				relatedStatus = Status.ACTIVE;
				invokeMethod = Func.DISCONNECT;
				optionList = null;
			}
			else if (_invokeMethod == Func.CONNECT) {
     			String[] connectElement = scString.split(SEPARATOR_ELEMENT);
	    		if (connectElement.length != 3) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
		    	ArrayList<String> _optionList = new ArrayList<String>();
			    _optionList.add(connectElement[1]);
			    _optionList.add(connectElement[2]);
			    commandWord = connectElement[0];
			    relatedStatus = Status.NONACTIVE;
			    invokeMethod = Func.CONNECT;
			    optionList = _optionList;
			}
		}
		
		//with general String ("SpecialCommand" rule)
		private SpecialCommand(String scString) {
			if (scString == null) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
			String[] scElementSet = scString.split(SEPARATOR_ELEMENT);
			if (scElementSet.length < 3) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
			
			setOptionList(new ArrayList<String>());
			commandWord = scElementSet[0];
			if (scElementSet[1].equals("active")) {
				relatedStatus = Status.ACTIVE;
			}
			else if (scElementSet[1].equals("nonactive")) {
				relatedStatus = Status.NONACTIVE;
			}
			else {
				throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
			}
			if (scElementSet[2].equals("connect")) {
				if (scElementSet.length != 5) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
				setInvokeMethod(Func.CONNECT);
				getOptionList().add(scElementSet[3]);
				getOptionList().add(scElementSet[4]);
			}
			else if (scElementSet[2].equals("disconnect")) {
				if (scElementSet.length > 3) throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
				setInvokeMethod(Func.DISCONNECT);
			}
			else {
				throw new RuntimeException(MESSAGE_INVALID_SPECIALCOMMAND);
			}
		}

		public Func getInvokeMethod() {
			return invokeMethod;
		}

		private void setInvokeMethod(Func invokeMethod) {
			this.invokeMethod = invokeMethod;
		}

		public ArrayList<String> getOptionList() {
			return optionList;
		}

		private void setOptionList(ArrayList<String> optionList) {
			this.optionList = optionList;
		}
	}
}