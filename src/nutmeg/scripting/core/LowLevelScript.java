package nutmeg.scripting.core;
import java.io.File;
import java.util.HashMap;

import nutmeg.core.util.files.*;
public class LowLevelScript {
	private String[] source;
	private scriptState  state = scriptState.WAITING;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Version: "+1+"\n");
		for(int i = 0; i < source.length; i++) {
			builder.append("["+i+"] "+source[i]+"\n");
		}
		
		return builder.toString();
	}
	
	public LowLevelScript(String[] _source) {
		source = _source;
	}
	
	/**
	 * 
	 * @param String - Path To The Script
	 * @return Script - A Script Containing the textfile data of the file
	 */
	public LowLevelScript(String pathToScript) {
		this(FileLoader.get().loadTextFile(new File(pathToScript)));
	} 
	
	public String getLineAt(int index) {
		return source[index];
	}
	
	public boolean isOutOfRange(int index) {
		return index > source.length;
	}
	
	public int size() {
		return source.length;
	}
	 
	public enum scriptState {
		RUNNING, STOPPED, INLOOP, BURNING, WAITING
	}
}
