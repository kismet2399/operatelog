package io.github.kismet2399.support;

import java.util.Stack;

/**
 * @author kismet
 */
public class BraceUtils {

    /**
     * 校验花括号是否匹配
     *
     * @param s 被校验字符
     * @return 校验结果
     */
    public static BraceValidResult isBraceValid(String s) {
        if (s.length() <= 4) {
            return new BraceValidResult(false);
        }

        boolean braceMatch = false;
        int leftBraceCount = 0;
        int rightBraceCount = 0;

        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            // 存储左括号
            if (aChar == '{') {
                stack.push(aChar);
                leftBraceCount++;
            }
            if (stack.isEmpty() && (aChar == '}')) {
                break;
            }
            // 配对右括号
            if (aChar == '}') {
                stack.pop();
                rightBraceCount++;
            }
        }
        if (stack.isEmpty()) {
            braceMatch = true;
        }
        return new BraceValidResult(leftBraceCount, rightBraceCount, braceMatch);
    }

    /**
     * @param content 内容字符串
     * @return BraceResult对象，包含左大括号的index，与之匹配的右大括号的index以及两者之间的内容
     */
    public static BraceResult findBraceResult(String content) {
        // 找到第一个"{"
        int leftBraceIndex = content.indexOf("{");
        int matchedRightBraceIndex = -1;
        if (leftBraceIndex < 0) {
            return null;
        }
        // stack用于处理第一个"{"右边可能出现的"{"
        Stack<Character> stack = new Stack<>();
        stack.push('{');
        // 从第一个"{"开始遍历，找到其匹配的"}"的index
        for (int i = leftBraceIndex + 1; i < content.length(); i++) {
            // 第一个"{"的右边还可能有"{"
            if (content.charAt(i) == '{') {
                stack.push('{');
            }
            if (content.charAt(i) == '}') {
                // 遇到"}"，栈中的"{"就需要弹出一个
                stack.pop();
            }
            if (stack.isEmpty()) {
                // 当栈空了，说明我们要找的和第一个"{"匹配的"}"就找到了
                matchedRightBraceIndex = i;
                break;
            }
        }
        // 没有找到相匹配的"}"
        if (matchedRightBraceIndex == -1) {
            return null;
        }
        String betweenBraceContent = content.substring(leftBraceIndex + 1, matchedRightBraceIndex);
        return new BraceResult(leftBraceIndex, matchedRightBraceIndex, betweenBraceContent);
    }
}
