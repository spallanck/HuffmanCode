// Sophie Pallanck
// 12/5/19
// CSE 143
// TA: James Qiu
// Assignment #8
//
/* This program implements a program called HuffmanCode
that allows users to compress and decompress files using
the Huffman Coding algorithm, which is based on the frequency
of the characters in the file. */
import java.util.*;
import java.io.*;

public class HuffmanCode {
   private static final int DEFAULT_FREQ = -1;
   
   private HuffmanNode overallRoot;
   
   /* post: takes an array of frequencies related to
   their corresponding ASCII value and constructs a
   huffman tree using these frequencies */
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> tree = new PriorityQueue<>();
      for (int i = 0; i < frequencies.length; i++) {
         if (frequencies[i] > 0) {
            tree.add(new HuffmanNode(i, frequencies[i]));
         }
      }
      while (tree.size() > 1) {
         HuffmanNode one = tree.remove();
         HuffmanNode two = tree.remove();
         int freqSum = one.freq + two.freq;
         HuffmanNode newNode = new HuffmanNode(DEFAULT_FREQ, freqSum, one, two); // is this t00 lonng
         tree.add(newNode);
      }
      overallRoot = tree.remove();
   }
   
   /* pre: scanner is not null and contains data
   in a legal and standard format */
   /* post: takes a scanner input and reconstructs a huffman
   tree using the given input */
   public HuffmanCode(Scanner input) {
      while (input.hasNextLine()) {
         int character = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = constructTree(overallRoot, code, character);
      }
   }
   
   /* pre: takes the current node of the huffman tree, a string
   representing the huffman code path to build up a tree, and an integer
   representing the current character, and builds up a new huffman tree */
   private HuffmanNode constructTree(HuffmanNode root, String code, int character) {
      if (code.isEmpty()) {
         root = new HuffmanNode(character, DEFAULT_FREQ);
      } else {
         if (root == null) {
            root = new HuffmanNode(DEFAULT_FREQ, DEFAULT_FREQ);
         }
         if (code.charAt(0) == '0') {
            root.zero = constructTree(root.zero, code.substring(1), character);
         } else {
            root.one = constructTree(root.one, code.substring(1), character);
         }
      }
      return root;
   }
   
   /* post: takes a PrintStream object output,
   and stores the huffman codes to the output
   PrintSteam in a standard format */
   public void save(PrintStream output) {
      saveTree(output, overallRoot, "");
   }
   
   /* post: takes a PrintStream output object, the
   current huffman node, and a string representing a code and saves the tree to the
   output stream in a stanrd format */
   private void saveTree(PrintStream output, HuffmanNode root, String code) {
      if (root != null) { //implicit base case
         if (root.zero == null && root.one == null) {
            output.println(root.character);
            output.println(code);
         }
         saveTree(output, root.zero, code + "0");
         saveTree(output, root.one, code + "1");
      }
   }
   
   // pre: input stream has a legal sequences of encoding characters
   /* post: takes a BitInputStream input and writes the corresponding
   characters to the given PrintStream output object */
   public void translate(BitInputStream input, PrintStream output) {
      int letter = translateHelper(overallRoot, input);
      output.write(letter);
      while (input.hasNextBit()) {
         letter = translateHelper(overallRoot, input);
         output.write(letter);
      }
   }
   
   /* post: takes a current huffman node and a BitInputStream and
   translates the bits from the input stream to an output stream */
   private int translateHelper(HuffmanNode root, BitInputStream input) {
      if (root.zero == null && root.one == null) {
         return root.character;
      } else {
         int nextBit = input.nextBit();
         if (nextBit == 0) {
            return translateHelper(root.zero, input);
         } else {
            return translateHelper(root.one, input);
         }
      }
   }
   
   /* This class represents a single node in the HuffmanCode tree,
   and is comparable to other HuffmanNodes */
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public int character;
      public int freq;
      public HuffmanNode zero;
      public HuffmanNode one;
      
      /* post: constructs a HuffmanNode with a
      given character, and frequency */
      public HuffmanNode(int character, int freq) {
         this(character, freq, null, null);
      }
      
      /* post: constructs a HuffmanNode with a given
      character, frequency, and links to other HufmanNodes */
      public HuffmanNode(int character, int freq, HuffmanNode zero, HuffmanNode one) {
         this.character = character;
         this.freq = freq;
         this.zero = zero;
         this.one = one;
      }
      
      /* post: compares one HuffmanNode to
      another based on frequency, with lower
      frequency nodes being considered 'less'
      than higher frequency nodes */
      public int compareTo(HuffmanNode other) {
         return this.freq - other.freq;
      }
   }
}