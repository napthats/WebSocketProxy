package com.napthats.websocket;

import java.util.ArrayList;
import java.util.Properties;


public class SpecialCommandSet {
	public static enum Status {ACTIVE, NONACTIVE};
	private ArrayList<SpecialCommand> scSet;
	
	public SpecialCommandSet(Properties prop) {
		String scStringSet = prop.getProperty("SpecialCommand");
		if (scStringSet == null) throw new RuntimeException("invalid properties: no SpecialCommand");
		scSet = new ArrayList<SpecialCommandSet.SpecialCommand>();
		for (String scString : scStringSet.split(",")) {
			scSet.add(new SpecialCommand(scString));
		}
	}
	
	public ArrayList<String> findSpecialCommand(Status targetStatus, String targetString) {
		for (SpecialCommand sc : scSet) {
			if (sc.commandWord.equals(targetString) && sc.relatedStatus == targetStatus) return sc.funcAndOption; 
		}
		return null;
	}
	
	private class SpecialCommand {
		private String commandWord;
		private Status relatedStatus;
		private ArrayList<String> funcAndOption;
		
		private SpecialCommand(String scString) {
			if (scString == null) throw new RuntimeException("invalid properties: invalid SpecialCommand");
			String[] scElementSet = scString.split(":");
			if (scElementSet.length < 3) throw new RuntimeException("invalid properties: invalid SpecialCommand");
			
			funcAndOption = new ArrayList<String>();
			commandWord = scElementSet[0];
			if (scElementSet[1].equals("active")) {
				relatedStatus = Status.ACTIVE;
			}
			else if (scElementSet[1].equals("nonactive")) {
				relatedStatus = Status.NONACTIVE;
			}
			else {
				throw new RuntimeException("invalid properties: invalid SpecialCommand");
			}
			if (scElementSet[2].equals("connect")) {
				if (scElementSet.length != 5) throw new RuntimeException("invalid properties: invalid SpecialCommand");
				funcAndOption.add(scElementSet[2]);
				funcAndOption.add(scElementSet[3]);
				funcAndOption.add(scElementSet[4]);
			}
			else if (scElementSet[2].equals("disconnect")) {
				if (scElementSet.length > 3) throw new RuntimeException("invalid properties: invalid SpecialCommand");
				funcAndOption.add(scElementSet[2]);
			}
			else {
				throw new RuntimeException("invalid properties: invalid SpecialCommand");
			}
		}
	}
}