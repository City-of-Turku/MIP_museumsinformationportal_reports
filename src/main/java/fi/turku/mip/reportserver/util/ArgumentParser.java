package fi.turku.mip.reportserver.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ArgumentParser {

	private HashMap<String, List<String>> argumentsAndOptions = new HashMap<String, List<String>>();
	public static final String DEFAULT_ARG = "DEFAULT-ARGUMENT";
	
	public ArgumentParser(String[] args) {
		String currentArg = DEFAULT_ARG;
		argumentsAndOptions.put(currentArg, new ArrayList<String>());
		
		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
	        	case '-':
	        		currentArg = args[i];
	        		argumentsAndOptions.put(currentArg, new ArrayList<String>());
	        		break;
	        	default:
	        		// this is an option for argument
	        		argumentsAndOptions.get(currentArg).add(args[i]);
	        		break;
			}
		}
	}


	public boolean hasArgument(String arg) {
		return argumentsAndOptions!=null && argumentsAndOptions.containsKey(arg);
	}
	
	public List<String> getArgumentOptions(String arg) {
		if (argumentsAndOptions==null) {
			return Collections.emptyList();
		}
		if (!argumentsAndOptions.containsKey(arg)) {
			return Collections.emptyList();
		}
		return argumentsAndOptions.get(arg);
	}
	
}
