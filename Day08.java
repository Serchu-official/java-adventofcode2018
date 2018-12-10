import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 {
    public static void main(String[] args) {
        List<Integer> licenseFile = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt())
            licenseFile.add(scanner.nextInt());

        System.out.println(day8Part1(licenseFile));
        System.out.println(day8Part2(licenseFile));
    }

    private static Integer day8Part1(List<Integer> licenseFile) {
        Tree tree = new Tree(licenseFile);
        return tree.getMetadataSum();
    }

    private static Integer day8Part2(List<Integer> licenseFile) {
        Tree tree = new Tree(licenseFile);
        return tree.getNodeValue();
    }
}

class Tree {
    private TreeNode root;

    Tree(List<Integer> licenseFile) {
        TreeNode currentNode = new TreeNode(licenseFile.get(0), licenseFile.get(1));

        for (int i = 2; i < licenseFile.size(); i++) {
            if (currentNode.missesChildren()) {
                TreeNode newNode = new TreeNode(licenseFile.get(i), licenseFile.get(i + 1));
                newNode.setParent(currentNode);
                currentNode.addChild(newNode);
                currentNode = newNode;
                i++;
            } else if (currentNode.missesMetadata()) {
                for (int j = i; j < i + currentNode.getNumMetadata(); j++) {
                    currentNode.addMetadata(licenseFile.get(j));
                }
                i += currentNode.getNumMetadata() - 1;

                if (currentNode.getParent() != null) {
                    currentNode = currentNode.getParent();
                }
            }
        }

        root = currentNode;
    }

    Integer getMetadataSum() {
        return root.getMetadataSum();
    }

    Integer getNodeValue() {
        return root.getNodeValue();
    }

    private class TreeNode {
        private Integer numChildren;
        private Integer numMetadata;
        private TreeNode parent;
        private List<TreeNode> children;
        private List<Integer> metadata;

        TreeNode(Integer numChildren, Integer numMetadata) {
            this.numChildren = numChildren;
            this.numMetadata = numMetadata;
            this.parent = null;
            this.children = new ArrayList<>();
            this.metadata = new ArrayList<>();
        }

        Integer getNumMetadata() {
            return numMetadata;
        }

        void setParent(TreeNode parent) {
            this.parent = parent;
        }

        TreeNode getParent() {
            return parent;
        }

        void addChild(TreeNode child) {
            this.children.add(child);
        }

        void addMetadata(Integer metadata) {
            this.metadata.add(metadata);
        }

        boolean missesChildren() {
            return !numChildren.equals(children.size());
        }

        boolean missesMetadata() {
            return !numMetadata.equals(metadata.size());
        }

        Integer getMetadataSum() {
            return metadata.stream().mapToInt(x -> x).sum() + children.stream().mapToInt(TreeNode::getMetadataSum).sum();
        }

        Integer getNodeValue() {
            if (children.isEmpty()) {
                return metadata.stream().mapToInt(x -> x).sum();
            } else {
                Integer nodeValue = 0;
                for (Integer metadatum : metadata) {
                    if (metadatum > 0 && metadatum <= numChildren) {
                        nodeValue += children.get(metadatum - 1).getNodeValue();
                    }
                }
                return nodeValue;
            }
        }
    }
}
