// CODE IS TOO SLOW; TIMELIMIT EXCEEDED OF 1 SECOND

// Ted Tran
// 12/9/2021
// My Solution to 2020 Virginia Tech High School Programming Contest: Ball Colors
// https://open.kattis.com/problems/ballcolors

import java.util.*;

public class BallColors {
   // Initialize variables
   public static ArrayList<Integer[]> sequenceIndexes;
   public static ArrayList<Integer[]> sequences;
      
   public static int amntOfColors;
   public static int[] colors;
   public static int[] balls;
   
   public static int amntOfDislikedColors;
   public static Integer[] dislikedColors;
   
   public static int amntOfLikedColors;
   public static Integer[] likedColors;


   public static void main(String[] args) {
   
      // Get input
      Scanner stdin = new Scanner(System.in);
      amntOfColors = stdin.nextInt();
      
      colors = new int[amntOfColors];
      for(int i=0; i<amntOfColors; i++){
         colors[i] = stdin.nextInt();
      }
      
      balls = new int[sumOfArray(colors)];
      int index = 0;
      for(int i=0; i<amntOfColors; i++){
         for(int j=0; j<colors[i]; j++){
            balls[index] = i+1;
            index++;
         }
      }
      
         //BALLS CONVERSION: [2, 1, 2, 1] -> [1, 1, 2, 3, 3, 4]
         //DISLIKES CONVERSION: [1, 2] -> [1, 2]
         //LIKES CONVERSION: [3, 4] -> [3, 4]
      
      amntOfDislikedColors = stdin.nextInt();
      dislikedColors = new Integer[amntOfDislikedColors];
      for (int i=0; i<amntOfDislikedColors; i++){
         dislikedColors[i] = stdin.nextInt();
      }
      
      amntOfLikedColors = stdin.nextInt();
      likedColors = new Integer[amntOfLikedColors];
      for (int i=0; i<amntOfLikedColors; i++){
         likedColors[i] = stdin.nextInt();
      }
          
      // Set up permutations
      Set<Integer> s = new HashSet<Integer>();
      for(int i = 0; i < balls.length; i++) {
         s.add(i);
      }
       
      // Gets every possible combination of ball colors
      sequenceIndexes = new ArrayList<Integer[]>(balls.length);
      sequences = new ArrayList<Integer[]>(balls.length);
      permutations(s, new Stack<Integer>(), balls.length);
      for(int i = 0; i < sequenceIndexes.size(); i++) {
         for(int j = 0; j < sequenceIndexes.get(i).length; j++) {
            sequences.get(i)[j] = balls[sequenceIndexes.get(i)[j]];
         }
      }
       
       // Get rid of duplicates
      ArrayList<Integer[]> nonDuplicates = removeDuplicates(sequences);
      
      // Grabs sum of possible orders with respect to conditions
      int sum = 0;
      for(int i = 0; i < nonDuplicates.size(); i++) {
         if (!isDisliked(nonDuplicates.get(i), dislikedColors)) {
            int n = nonDuplicates.get(i).length;
            int m = likedColors.length;
            if (isLiked(nonDuplicates.get(i), likedColors)) {
               sum = sum + 1;
            }
         }
      }
      System.out.println(sum);
   }
   
   // Checks if an integer array is disliked
   public static boolean isLiked(Integer[] arr, Integer[] likedColors) {
      // If no favorites then all will be liked
      if (likedColors.length == 0) {
         return true;
      }
      
      // Checks if colors are in favorite sequence as many times as possible
      boolean hadLikeOrder = false;
      while (hasLikedSubset(arr, arr.length, likedColors, likedColors.length)) {
         
         int indexStartToBeChanged = isSubArray(arr, likedColors, arr.length, likedColors.length);
         if(indexStartToBeChanged > -1) {
            int indexEndToBeChanged = indexStartToBeChanged+likedColors.length-1;
            
            // Change likeOrder in array to a dummy value
            for(int i = indexStartToBeChanged; i < indexEndToBeChanged + 1; i++) {
               arr[i] = -1;
            }
            hadLikeOrder = true;
         } else {
            return false;
         }
      }
      
      if (hadLikeOrder == true)
         return true;
      return false;
   }
   
   // Checks if an integer is disliked
   public static boolean ballIsDisliked(int ball, Integer[] dislikedColors) {
      for (Integer element : dislikedColors) {
         if (ball == element) {
            return true;
         }
      }
      return false;
   }
   
   // Checks if an integer array is disliked
   public static boolean isDisliked(Integer[] arr, Integer[] dislikedColors) {
      for(int i = 1; i < arr.length-1; i++){
         int currentBall = arr[i];
         int oneBehind = arr[i-1];
         int oneAhead = arr[i+1];
         if(ballIsDisliked(currentBall, dislikedColors)) {
            if(ballIsDisliked(oneBehind, dislikedColors)) {
               return true;
            }
            if (ballIsDisliked(oneAhead, dislikedColors)) {
               return true;
            }
         }
      }
      return false;
   }
   
   // Return true if arr2[] is a subset of arr1[] - ORDER DOES NOT MATTER
   // code from function courtesy to akhilsaini from geeksforgeeks
   static boolean hasLikedSubset(Integer[] arr1, int m, Integer[] arr2, int n) {
         
       // Create a Frequency Table using STL
      HashMap<Integer, 
               Integer> frequency = new HashMap<Integer, 
                                                Integer>();
     
       // Increase the frequency of each element
       // in the frequency table.
      for(int i = 0; i < m; i++) 
      {
         frequency.put(arr1[i],
                         frequency.getOrDefault(
                             arr1[i], 0) + 1);
      }
         
       // Decrease the frequency if the
       // element was found in the frequency
       // table with the frequency more than 0.
       // else return 0 and if loop is
       // completed return 1.
      for(int i = 0; i < n; i++) 
      {
         if (frequency.getOrDefault(arr2[i], 0) > 0)
            frequency.put(arr2[i],
                             frequency.get(arr1[i]) - 1);
         else
         {
            return false;
         }
      }
      return true;
   }
   
   // Gets all possible combinations
   // Ex. All possible combinations of 1, 2, 3 ->
   // [1, 2, 3]
   // [1, 3, 2]
   // [2, 1, 3]
   // [2, 3, 1]
   // [3, 1, 2]
   // [3, 2, 1]
   public static void permutations(Set<Integer> items, Stack<Integer> permutation, int size) {
       /* permutation stack has become equal to size that we require */
      if(permutation.size() == size) {
           /* print the permutation */
         sequenceIndexes.add(permutation.toArray(new Integer[0]));
         sequences.add(new Integer[balls.length]);
      }
   
       /* items available for permutation */
      Integer[] availableItems = items.toArray(new Integer[0]);
      for(Integer i : availableItems) {
           /* add current item */
         permutation.push(i);
      
           /* remove item from available item set */
         items.remove(i);
      
           /* pass it on for next permutation */
         permutations(items, permutation, size);
      
           /* pop and put the removed item back */
         items.add(permutation.pop());
      }
   }
   
   // Returns sum of all the elements combined
   public static int sumOfArray(int[] arr) {
      int sum = 0;
      for(int i = 0; i < arr.length; i++) {
         sum = sum + arr[i];
      }
      
      return sum;
   }
   
   // Returns if the same array of integers is already inside the arraylist of integer arrays
   public static boolean isInArrayList(ArrayList<Integer[]> list, Integer[] candidate) {
      for(Integer[] item : list){
         if(Arrays.equals(item, candidate)){
            return true;
         }
      }
      return false;
   }
   
   // Returns a removed duplicate version of the arraylist integer arrays passed
   public static ArrayList<Integer[]> removeDuplicates(ArrayList<Integer[]> list) {
      ArrayList<Integer[]> newList = new ArrayList<Integer[]>();
   
        // Traverse through the first list
      for (Integer[] element : list) {
      
            // If this element is not present in newList
            // then add it
         if (!isInArrayList(newList, element)) {
         
            newList.add(element);
         }
      }
   
        // return the new list
      return newList;
   }
   
   // Function to check if an array is
    // subarray of another array
    // code from function courtesy to gp6 from geeksforgeeks
   static int isSubArray(Integer A[], Integer B[], int n, int m)
   {
         //System.out.println(Arrays.toString(A));
         //System.out.println(Arrays.toString(B));
        // Two pointers to traverse the arrays
      int i = 0, j = 0;
      
        // Traverse both arrays simultaneously
      while (i < n && j < m)
      {
      
            // If element matches
            // increment both pointers
         if (A[i] == B[j])
         {
         
            i++;
            j++;
         
                // If array B is completely
                // traversed
            if (j == m)
               return i-j;
         }
             
            // If not,
            // increment i and reset j
         else
         {
            i = i - j + 1;
            j = 0;
         }
      }
      //System.out.println("returning false");
      return -1;
   }
}
