import java.util.*;

class TNode {
    boolean isEow;
    TNode[] children;
    static final int ALPHA_SIZE = 26;

    public TNode() {
        isEow = false;
        children = new TNode[ALPHA_SIZE];
    }
}

class Trie {
    TNode root;

    public Trie() {
        root = new TNode();
    }

    void insertWord(String word) {
        TNode temp = root;
        for (char ch : word.toCharArray()) {
            int indx = ch - 'a';
            if (temp.children[indx] == null) {
                TNode nn = new TNode();
                temp.children[indx] = nn;
                temp = nn;
            } else {
                temp = temp.children[indx];
            }
        }
        temp.isEow = true;
    }

    boolean hasWord(String word) {
        TNode temp = root;
        for (char ch : word.toCharArray()) {
            int indx = ch - 'a';
            if (temp.children[indx] == null) {
                return false;
            }
            temp = temp.children[indx];
        }
        return temp.isEow;
    }

    List<String> getAllWords() {
        List<String> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        getAllWordsHelper(root, sb, ans);
        return ans;
    }

    private void getAllWordsHelper(TNode root, StringBuilder path, List<String> words) {
        if (root.isEow) {
            words.add(path.toString());
        }
        for (int i = 0; i < TNode.ALPHA_SIZE; i++) {
            if (root.children[i] != null) {
                char ch = (char) (i + 'a');
                path.append(ch);
                getAllWordsHelper(root.children[i], path, words);
                path.setLength(path.length() - 1);
            }
        }
    }

    List<String> autoSuggest(String prefix) {
        TNode temp = root;
        List<String> ans = new ArrayList<>();
        for (char ch : prefix.toCharArray()) {
            int indx = ch - 'a';
            if (temp.children[indx] == null) {
                return ans;
            }
            temp = temp.children[indx];
        }
        StringBuilder sb = new StringBuilder(prefix);
        getAllWordsHelper(temp, sb, ans);
        return ans;
    }
}

public class Trie1 {
    public static void main(String[] args) {
        Trie t = new Trie();
        t.insertWord("car");
        t.insertWord("pretty");
        t.insertWord("prettier");
        t.insertWord("prettiest");

        System.out.println("All words in the Trie:");
        for (String s : t.getAllWords()) {
            System.out.println(s);
        }

        System.out.println("\nAuto-suggestions for 'pre':");
        for (String s : t.autoSuggest("pre")) {
            System.out.println(s);
        }

        System.out.println("\nDoes the Trie contain 'zebra'? " + t.hasWord("zebra"));
    }
}