import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	public static Queue<String> queue = new LinkedList<>();
	public static Set<String> marked = new HashSet<>();
	public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";

	public static void bfsAlgorithm(String root) throws IOException {
		queue.add(root);
		BufferedReader br = null;

		while (!queue.isEmpty()) {
			String crawledURL = queue.poll();
			System.out.println("\n=== Site Crawled: " + crawledURL + " ===");

			// limit to 100 web sites
			if (marked.size() > 1000)
				return;

			boolean ok = false;
			URL url = null;

			while (!ok) {
				try {
					url = new URL(crawledURL);
					br = new BufferedReader(new InputStreamReader(url.openStream()));
					ok = true;
				} catch (MalformedURLException mue) {
					System.out.println("*** Malformed URL: " + crawledURL);
					crawledURL = queue.poll();
					ok = false;

				} catch (IOException ioe) {
					System.out.println("*** IOException for URL: " + crawledURL);
					crawledURL = queue.poll();
					ok = false;

				}
			}

			StringBuilder sb = new StringBuilder();
			String temp = null;

			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}

			temp = sb.toString();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(temp);

			while (matcher.find()) {
				String w = matcher.group();

				if (!marked.contains(w)) {
					marked.add(w);
					System.out.println("Site added for crawling: " + w);
					queue.add(w);
				}

			}

		}
		if (br != null) {
			br.close();
		}
	}

	public static void showResults() {
		System.out.println("\n\nResults: ");
		System.out.println("Web sites crawled: " + marked.size() + "\n");

		for (String s : marked) {
			System.out.println("*" + s);
		}
	}

	public static void main(String[] args) {
		try {
			bfsAlgorithm("https://ishahomeschool.org/");
			showResults();
		} catch (IOException e) {
		}
	}
}
