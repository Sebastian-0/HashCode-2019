import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Solution_2 {
	public static void main(String[] args) throws IOException {
		Map<String, Integer> index = new HashMap<>();
		//Kattio in = new Kattio(new FileInputStream()))
		
		List<Picture> pics = new LinkedList<>();
		
		String file = "b_lovely_landscapes";
		
		BufferedReader in = new BufferedReader(new FileReader("input/" + file + ".txt"));
		int N = Integer.parseInt(in.readLine());
		for (int i = 0; i < N; i++) {
			Picture picture = new Picture(pics.size());
			String[] tokens = in.readLine().split(" ");
			picture.isHorizontal = tokens[0].equals("H");
			for (int j = 2; j < tokens.length; j++) {
				String tag = tokens[j];
				int idx = index.computeIfAbsent(tag, k->index.size());
				picture.tags.add(idx);
			}
			pics.add(picture);
		}
		in.close();
		
		
		
		FileWriter out = new FileWriter("output/" + file.charAt(0) + "_out2.txt");
		out.write(pics.size() + "\n");
		
		Picture curr = pics.get(0);
		curr.taken = true;
		
		out.write(curr.id + "\n");

		int totalScore = 0;
		for (int i = 0; i < pics.size(); i++) {
			Picture maxP = null;
			int maxScore = -1;
			for (Picture p : pics) {
				if (!p.taken) {
					int score = computeScore(curr, p);
					if (score > maxScore) {
						maxScore = score;
						maxP = p;
					}
					
					if (score == 3) {
						break;
					}
				}
			}
			
			if (maxP != null) {
				out.write(maxP.id + "\n");
				maxP.taken = true;
				curr = maxP;
				totalScore += maxScore;
			}
			
			if (i % 100 == 0) {
				System.out.println(i + "/" + pics.size());
			}
		}
		
		out.close();
		
		System.out.println(totalScore);
	}


	private static int computeScore(Picture last, Picture picture) {
		int common = 0;
		for (Integer idx : picture.tags) {
			if (last.tags.contains(idx)) {
				common++;
			}
		}
		
		int firstOnly = last.tags.size() - common;
		int secondOnly = last.tags.size() - common;
		
		return Math.min(common, Math.min(firstOnly, secondOnly));
//		return 10;
	}
	
	
	private static class Picture {
		public int id;
		public Set<Integer> tags = new HashSet<>();
		public boolean isHorizontal;
		public boolean taken;
		
		public Picture(int id) {
			this.id = id;
		}
	}
}
