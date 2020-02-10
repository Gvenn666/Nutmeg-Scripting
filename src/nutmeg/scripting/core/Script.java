package nutmeg.scripting.core;
import java.io.File;

import nutmeg.core.util.files.*;
public class Script {
	private String[] source;
	private scriptState  state = scriptState.WAITING;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Version: "+1);
		for(int i = 0; i < source.length; i++) {
			builder.append("["+i+"] "+source[i]+"\n");
		}
		
		return builder.toString();
	}
	
	public Script(String[] _source) {
		source = source;
	}
	
	public Script(String pathToScript) {
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
