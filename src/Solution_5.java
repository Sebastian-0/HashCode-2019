import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Solution_5 {
	public static void main(String[] args) throws IOException {
		Map<String, Integer> index = new HashMap<>();
		
		List<Picture> pictures = new ArrayList<>();
		List<Picture> verticals = new ArrayList<>();
		
//		String file = "c_memorable_moments";
//		String file = "d_pet_pictures";
		String file = "e_shiny_selfies";
		BufferedReader in = new BufferedReader(new FileReader("input/" + file + ".txt"));
		int N = Integer.parseInt(in.readLine());
		for (int i = 0; i < N; i++) {
			Picture picture = new Picture(i);
			String[] tokens = in.readLine().split(" ");
			picture.isHorizontal = tokens[0].equals("H");
			for (int j = 2; j < tokens.length; j++) {
				String tag = tokens[j];
				int idx = index.computeIfAbsent(tag, k->index.size());
				picture.tags.add(idx);
			}
			if (!picture.isHorizontal) {
				verticals.add(picture);
			} else {
				pictures.add(picture);
			}
		}
		in.close();

		// Random
//		Picture vPic = null;
//		for (Picture picture : verticals) {
//			if (vPic == null) {
//				vPic = picture;
//			} else {
//				pics.add(new Picture(vPic, picture));
//				vPic = null;
//			}
//		}
		
		// SMall big
		verticals.sort((p1, p2) -> p1.tags.size() - p2.tags.size());
		for (int i = 0; i < verticals.size()/2; i++) {
			pictures.add(new Picture(verticals.get(i), verticals.get(verticals.size() - i - 1)));
		}
		
		//Least common
		int divisions = 32;
		int size = verticals.size() / divisions;
//		for (int i = 0; i < divisions; i++) {
//			int start = i * size;
//			int end = (i + 1) == divisions ? verticals.size() : (i+1) * size;
//			computeSubsolutionLeastCommon(verticals, start, end, pictures);
//		}
		
		Collections.shuffle(pictures);

		Picture curr = pictures.get(0);
		curr.taken = true;
		
		int totalScore = 0;
		
		FileWriter out = new FileWriter("output/" + file.charAt(0) + "_out4_small_big2.txt");
		out.write(pictures.size() + "\n");
		out.write(curr.toString() + "\n");
		
		divisions = 1;
		size = pictures.size() / divisions;
		for (int i = 0; i < divisions; i++) {
			int start = i * size;
			int end = (i + 1) == divisions ? pictures.size() : (i+1) * size;
			totalScore += computeSubsolution(pictures, curr, start, end, out);
		}
		
		out.close();
		
		System.out.println(totalScore);
	}
	
	private static int computeSubsolution(List<Picture> pics, Picture curr, int s, int e, FileWriter out) throws IOException {
		System.out.println(s + " " + e);
		int totalScore = 0;
		for (int i = s; i < e; i++) {
			Picture maxP = null;
			int maxScore = -1;
			for (int j = s; j < e; j++) {
				Picture p = pics.get(j);
				if (!p.taken) {
					int score = score(curr, p);
					if (score > maxScore) {
						maxScore = score;
						maxP = p;
					}
					
//					if (score == 3) {
//						break;
//					}
				}
			}
			
			if (maxP != null) {
				out.write(maxP.toString() + "\n");
				maxP.taken = true;
				curr = maxP;
				totalScore += maxScore;
			}
			
			if (i % 100 == 0) {
				System.out.println(i + "/" + pics.size() + " - " + maxScore);
			}
		}
		
		return totalScore;
	}
	
	private static void computeSubsolutionLeastCommon(List<Picture> verticals, int s, int e, List<Picture> pictures) {
		for (int i = s; i < e; i++) {
            Picture p1 = verticals.get(i);
            if (p1.taken) {
                continue;
            }
            int min = Integer.MAX_VALUE;
            Picture best = null;
            for (int j = i + 1; j < e; j++) {
//            for (int j = e-1; j > i; j--) {
                Picture p2 = verticals.get(j);
                if (p2.taken) {
                    continue;
                }
                int score = score(p1, p2);
                if (score < min) {
                    best = p2;
                    min = score;
                }
            }
            pictures.add(new Picture(p1, best));
            p1.taken = true;
            best.taken = true;
			
			if (i % 100 == 0) {
				System.out.println(i + "/" + verticals.size());
			}
        }
	}


	private static int score(Picture last, Picture picture) {
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
		public int id2;
		public Set<Integer> tags = new HashSet<>();
		public boolean isHorizontal;
		public boolean taken;
		
		public Picture(int id) {
			this.id = id;
			this.id2 = -1;
		}
		
		public Picture(Picture o1, Picture o2) {
			this.id = o1.id;
			this.id2 = o2.id;
			this.tags.addAll(o1.tags);
			this.tags.addAll(o2.tags);
		}
		
		@Override
		public String toString() {
			if (id2 >= 0) {
				return id + " " + id2;
			}
			return id + "";
		}
	}
}
