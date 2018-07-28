package com.mydemo.jjc.javacase;

public class TestCase01 {

    public static void main(String[] args) {
        int[][] matrix1 = {{1,2,3,4},{5,6,7,8}};
        int[][] matrix2 = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        int[][] matrix3 = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};

        printMatrix(matrix3);
    }

    public static void printMatrix(int[][] matrix){
        if (matrix == null){
            return;
        }
        int rows = matrix.length;
        int colums = matrix[0].length; //矩阵：将一些元素排列成若干行，每行放上相同数量的元素，就是一个矩阵

        /**
         * 代表第几环打印，从1开始
         * 打印1环 至少需要1行 1列
         * 打印2环 至少需要3行 3列
         * 打印3环 至少需要5行 5列
         * 打印4环 至少需要7行 7列
         * ....
         * 打印n环 至少需要2n-1行 2n-1列
         */
        int i = 0;
        while (rows >= (i+1)*2-1 && colums >= (i+1)*2-1){
            printRing(matrix,i,rows,colums);
            ++i;
        }
    }

    private static void printRing(int[][] matrix, int start, int rows, int colums) {
        int endRow = rows - 1 - start;
        int endCol = colums - 1 - start;
        //从左向右打印（上面行）
        for (int i = start;i <= endCol;i++){
            System.out.print(matrix[start][i]+",");
        }
        //如果环尾最后一行的行号大于环首的行号（有多行），则可以从右侧自上而下打印
        if (endRow > start){
            for (int i = start+1;i <= endRow;i++){
                System.out.print(matrix[i][endCol]+",");
            }
        }

        //如果有多行，并且有多列，则可以自右向左打印
        if (endRow > start && endCol > start){
            for (int i = endCol-1;i >= start;i--){
                System.out.print(matrix[endRow][i]+",");
            }
        }
        //如果至少有3行，并且至少有2列 则左侧可以由下而上打印
        if (endCol > start && endRow - 1 > start){
            for (int i = endRow-1;i >= start+1;i--){
                System.out.print(matrix[i][start]+",");
            }
        }
    }
}
