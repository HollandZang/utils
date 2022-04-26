package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProcessUtils
{
	public static List<Process> getProcessLists()
	{
		List<Process> tasklist = new ArrayList<Process>();
		java.lang.Process process = null;
		BufferedReader reader = null;
		try
		{
			Runtime runtime = Runtime.getRuntime();
			/*
			 * 自适应执行查询进程列表命令
			 */
			Properties prop = System.getProperties();
			//获取操作系统名称
			String os = prop.getProperty("os.name");
			boolean isWindows = !(os != null && (os.toLowerCase().contains("linux") || os.toLowerCase().contains("mac")));
			//显示所有进程
			if (isWindows)
				process = runtime.exec("tasklist");
			else
				process = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "ps -ef | awk '{print $2\" \"$7\" \" $0}'" });
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				if ("".equals(line))
				{
					continue;
				}
				String[] ss = line.split(" ");
				String pid, cmd;
				if (isWindows)
				{
					pid = ss[0];
					cmd = ss[1];
				}
				else
				{
					pid = ss[0];
					String time = ss[1];
					String[] times = line.split(time + " ");
					cmd = times[2];
				}
				tasklist.add(new Process(pid, cmd));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (process != null)
				process.destroy();
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return tasklist;
	}

	public static class Process
	{
		String pid;
		String cmd;

		Process(String pid, String cmd)
		{
			this.pid = pid;
			this.cmd = cmd;
		}

		public String getPid()
		{
			return pid;
		}

		public String getCmd()
		{
			return cmd;
		}

		@Override
		public String toString()
		{
			return "Process{" + "pid='" + pid + '\'' + ", cmd='" + cmd + '\'' + '}';
		}
	}
}
