package xml;

import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class module {
	
	private String name = null;
	private String file = null;
	private String fullpath = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	public String getFullpath() {
		return fullpath;
	}
	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}
	
	public static void main(String args[]){
		module _module_ = new module();
		_module_.setName("Instance Counter");
		_module_.setFile("instance_counter.bsh");
		
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(_module_, new FileWriter("/home/user/workspaces/mxterminal/module.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
