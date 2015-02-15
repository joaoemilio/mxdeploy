package xml;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class modulegroups {

	List<modulegroup> list = new ArrayList<modulegroup>();
	
	public static void main(String args[]){
		modulegroup _module_group_ = new modulegroup();
		_module_group_.setName("multi-connection");
		_module_group_.setDir("multi-connection");
		
		modulegroups modules = new modulegroups();
		modules.addModule(_module_group_);
		
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(modules, new FileWriter("/home/user/workspaces/mxterminal/module-groups.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public List<modulegroup> getList() {
		return list;
	}

	public void addModule(modulegroup _module_group_) {
		this.list.add(_module_group_);
	}
	
	
	
	
}
