package com.mxdeploy;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.jcraft.jsch.JSchException;
import com.mxssh.SSHConsoleService;

public class Test {

	public static void main(String[] args) {

		SSHConsoleService ssh = new SSHConsoleService();
		try {
			ssh.setPrvkeyPath("/home/fbsantos/.ssh/id_rsa");
			ssh.connect("localhost", "fbsantos");

			String PIDs = ssh.executeCommand("ls -ltr | wc -l | xargs");
			System.out.println(PIDs);

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			ssh.disconnect();
		}
	}


}
