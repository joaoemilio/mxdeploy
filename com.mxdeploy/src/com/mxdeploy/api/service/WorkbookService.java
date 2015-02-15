package com.mxdeploy.api.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.WorkbookProject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wds.bean.Cell;
import com.wds.bean.Cluster;
import com.wds.bean.Node;
import com.wds.bean.WorkBook;
import com.wds.bean.jdbc.DB2UniversalJDBCDriver;
import com.wds.bean.jdbc.JDBCProvider;
import com.wds.bean.jdbc.OracleJDBCDriver;
import com.wds.bean.jdbc.SQLServerJDBCDriver;
import com.wds.bean.jdbc.SQLServerJTDSDriver;
import com.wds.bean.mq.JMSProvider;
import com.wds.bean.mq.MQQueue;
import com.wds.bean.mq.MQQueueConnectionFactory;
import com.wds.bean.mq.MQTopicConnectionFactory;
import com.wds.bean.security.JAASAuthData;
import com.wds.bean.server.Server;

public class WorkbookService {

	public WorkBook loadWorkBook(String path){
		WorkBook workbook = null;
		XStream xstream = new XStream(new DomDriver());
		try {
			workbook = (WorkBook)xstream.fromXML(new FileReader(path ));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return workbook;
	}

	public void remove(String path){
		File f = new File(path);
		f.delete();
	}
	
	public void writeXML(WorkBook workbook, String projectAlias) {
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(workbook, new FileWriter(Database.getProjectPath() + "/" + projectAlias + "/workbook.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToken(WorkbookProject workbookProject, String projectAlias) {
		BufferedWriter writer = null;
		try {
			File tokenFile = new File(Database.getProjectPath()+"/"+projectAlias+"/token.properties");
			writer = new BufferedWriter(new FileWriter(tokenFile));

			writer.write("[Basic]\n");
			writer.write("name="+workbookProject.getName()+"\n");
			writer.write("nodes="+workbookProject.getNodes()+"\n");
			writer.write("webservers="+workbookProject.getWebservers()+"\n");
			writer.write("vertical="+workbookProject.getVertical()+"\n" );

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
    private String getNodeNames(List<String> nodeNameList){
    	String nodeNames = "";
        for(String nodeName : nodeNameList)
            if( nodeNames.length() == 0)
                nodeNames = nodeName;
            else
                nodeNames = nodeNames+";"+nodeName;
                
        return nodeNames;
    }
    
	public void exportToken(WorkBook pWorkbook, Project project, String appname, String vertical, String webservers, List<String> nodeNameList){
		Cell cell = pWorkbook.getCell();
		Cluster cluster = cell.getCluster();    
		
		String nodeNames = getNodeNames(nodeNameList);
		int ivertical = Integer.valueOf(vertical);
		if( ivertical > 1)
			vertical = String.valueOf(ivertical);
		else
			vertical = "0";
			
		String PROP_FILE_NAME = "token.properties";
		String PROP_FILE_PATH = Database.getProjectPath()+"/"+project.getAlias()+"/"+PROP_FILE_NAME;
				
		FileWriter f = null;
		try {
			f = new FileWriter(PROP_FILE_PATH);
			f.write("[CONFIG]\n");
			f.write("appname="+appname+"\n");
			f.write("nodes="+nodeNames+"\n");
			f.write("vertical="+vertical+"\n");
			f.write("webservers="+webservers+"\n");
			f.write("workbookFile=token.xml\n");
			
			writeJAASAuthDatas(f, cell.getJAASAuthDatas());
			writeJAASAuthDatas(f, cluster.getSecurityDomain().getJAASAuthDatas());
			writeJDBCProvider(f, cluster.getJDBCProviders());
			
			JMSProvider jmsProvider = cluster.getJMSProvider();
			writeMQQueue(f,jmsProvider.getMQQueues());
			writeMQQueueConnectionFactory(f,jmsProvider.getMQQueueConnectionFactories());
			writeMQTopicConnectionFactory(f,jmsProvider.getMQTopicConnectionFactories());
			
			Node node = cluster.getNodes().get(0);
			Server server = node.getServers().get(0);
	        writeJDBCProvider(f, server.getJDBCProviders());
	        writeMQQueue(f,jmsProvider.getMQQueues());
	        writeMQQueueConnectionFactory(f,jmsProvider.getMQQueueConnectionFactories());
	        writeMQTopicConnectionFactory(f,jmsProvider.getMQTopicConnectionFactories());			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void writeJAASAuthDatas(FileWriter f,List<JAASAuthData> jaasAuthDatas) throws IOException{
		if( jaasAuthDatas != null && jaasAuthDatas.size() > 0){
			int count = 1;
			for(JAASAuthData jaasAuthData : jaasAuthDatas){
				f.write("\n");
				f.write("[J2C_AUTHENTICATION_DATA_#"+count+"]\n");
				f.write("alias="+jaasAuthData.getAlias()+"\n");
				f.write("userId="+jaasAuthData.getUserId()+"\n");
				f.write("password="+jaasAuthData.getPassword()+"\n");
				count++;
			}
		}
	}

	private void writeMQQueue(FileWriter f, List<MQQueue> mqQueues) throws IOException{
		if( mqQueues.size() > 0){
			int count = 1;
			for(MQQueue mqQueue : mqQueues){
				f.write("\n");
				f.write("[MQQUEUE_#"+count+"]\n");
				f.write("name="+mqQueue.getName()+"\n");
				f.write("jndiName="+mqQueue.getJndiName()+"\n");
				f.write("baseQueueManagerName="+mqQueue.getBaseQueueManagerName()+"\n");
				f.write("baseQueueName="+mqQueue.getBaseQueueName()+"\n");
				f.write("queueManagerHost="+mqQueue.getQueueManagerHost()+"\n");
				f.write("queueManagerPort="+mqQueue.getQueueManagerPort()+"\n");
				f.write("serverConnectionChannelName="+mqQueue.getServerConnectionChannelName()+"\n");
				count ++;
			}
		}
	}
        
	private void writeMQQueueConnectionFactory(FileWriter f, List<MQQueueConnectionFactory> mqQueueConnectionFactories) throws IOException{
		if( mqQueueConnectionFactories.size() > 0){
			int count = 1;
			for(MQQueueConnectionFactory mqQueueCF : mqQueueConnectionFactories){
				f.write("\n");
				f.write("[MQUEUE_CONNECTION_FACTORY_#"+count+"]\n");
				f.write("name="+mqQueueCF.getName()+"\n");
				f.write("jndiName="+mqQueueCF.getJndiName()+"\n");
				f.write("host="+mqQueueCF.getHost()+"\n");
				f.write("port="+mqQueueCF.getPort()+"\n"); 
				f.write("channel="+mqQueueCF.getChannel()+"\n");
				f.write("queueManager="+mqQueueCF.getQueueManager()+"\n");
				count ++;
			}
		}
	}

	private void writeMQTopicConnectionFactory(FileWriter f, List<MQTopicConnectionFactory> mqTopicConnectionFactories) throws IOException{
		if( mqTopicConnectionFactories.size() > 0){
			int count = 1;
			for( MQTopicConnectionFactory mqTopicCF : mqTopicConnectionFactories){
				f.write("\n");;
				f.write("[MQTOPIC_CONNECTION_FACTORY_#"+count+"]\n");;
				f.write("name="+mqTopicCF.getName()+"\n");;
				f.write("jndiName="+mqTopicCF.getJndiName()+"\n");;
				f.write("host="+mqTopicCF.getHost()+"\n");;
				f.write("port="+mqTopicCF.getPort()+"\n");;
				f.write("channel="+mqTopicCF.getChannel()+"\n");;
				f.write("queueManager="+mqTopicCF.getQueueManager()+"\n");;
				count ++;
			}
		}
	}
			
	private  void writeJDBCProvider(FileWriter f, List<JDBCProvider> jdbcProviders) throws IOException{
		if( jdbcProviders.size() > 0)
			for(JDBCProvider jdbcProvider : jdbcProviders) { 
			    List<DB2UniversalJDBCDriver> db2UniversalJDBCDrivers = jdbcProvider.getDb2UniversalJDBCDrivers();
			    List<OracleJDBCDriver> oracleJDBCDrivers = jdbcProvider.getOracleJDBCDrivers();
			    List<SQLServerJDBCDriver> sqlServerJDBCDrivers = jdbcProvider.getSQLServerJDBCDrivers();
			    List<SQLServerJTDSDriver >sqlServerJTDSDrivers = jdbcProvider.getSQLServerJTDSDrivers();

			    if( db2UniversalJDBCDrivers.size() > 0){
			        int count = 1;
			        for(DB2UniversalJDBCDriver db2UniversalJDBCDriver : db2UniversalJDBCDrivers){
			            f.write("\n");
			            f.write("[JDBC_DB2UNIVERSAL_#"+count+"]\n");
			            f.write("name="+db2UniversalJDBCDriver.getName()+"\n");
			            f.write("jndiname="+db2UniversalJDBCDriver.getJndiName()+"\n");
			            f.write("portNumber="+db2UniversalJDBCDriver.getJdbcResourcePropertySet().getPortNumber()+"\n");
			            f.write("serverName="+db2UniversalJDBCDriver.getJdbcResourcePropertySet().getServerName()+"\n");
			            f.write("databaseName="+db2UniversalJDBCDriver.getJdbcResourcePropertySet().getDatabaseName()+"\n");
			            count ++;     
			        }
			    }

			    if( oracleJDBCDrivers.size() > 0){
			        int count = 1;
			        for(OracleJDBCDriver oracleJDBCDriver : oracleJDBCDrivers){
			            f.write("\n");
			            f.write("[JDBC_ORACLE_#"+count+"]\n");
			            f.write("name="+oracleJDBCDriver.getName()+"\n");
			            f.write("jndiname="+oracleJDBCDriver.getJndiName()+"\n");
			            f.write("URL="+oracleJDBCDriver.getJdbcResourcePropertySet().getURL()+"\n");
			            count ++;            
			        }
			    }
					
			    if( sqlServerJDBCDrivers.size() > 0){
			        int count = 1;
			        for(SQLServerJDBCDriver sqlServerJDBCDriver : sqlServerJDBCDrivers){
			            f.write("\n");
			            f.write("[JDBC_SQLSERVER_#"+count+"]\n");
			            f.write("name="+sqlServerJDBCDriver.getName()+"\n");
			            f.write("jndiname="+sqlServerJDBCDriver.getJndiName()+"\n");
			            f.write("portNumber="+sqlServerJDBCDriver.getJdbcResourcePropertySet().getPortNumber()+"\n");
			            f.write("serverName="+sqlServerJDBCDriver.getJdbcResourcePropertySet().getServerName()+"\n");
			            f.write("databaseName="+sqlServerJDBCDriver.getJdbcResourcePropertySet().getDatabaseName()+"\n");
			            count ++;                         
			        }
			    }
			    
			    if( sqlServerJTDSDrivers.size() > 0){
			        int count = 1;
			        for(SQLServerJTDSDriver sqlServerJTDSDriver : sqlServerJTDSDrivers){
			            f.write("\n");
			            f.write("[JDBC_SQLSERVER_JTDS_#"+count+"]\n");
			            f.write("name="+sqlServerJTDSDriver.getName()+"\n");
			            f.write("jndiname="+sqlServerJTDSDriver.getJndiName()+"\n");
			            f.write("portNumber="+sqlServerJTDSDriver.getJdbcResourcePropertySet().getPortNumber()+"\n");
			            f.write("serverName="+sqlServerJTDSDriver.getJdbcResourcePropertySet().getServerName()+"\n");
			            f.write("databaseName="+sqlServerJTDSDriver.getJdbcResourcePropertySet().getDatabaseName()+"\n");
			            count ++;
			        }
			    }
			}
	}
	

}
