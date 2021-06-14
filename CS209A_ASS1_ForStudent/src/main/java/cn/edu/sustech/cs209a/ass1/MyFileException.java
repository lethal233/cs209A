package cn.edu.sustech.cs209a.ass1;

public class MyFileException extends Exception{
    /**
     * the error (abnormal) line
     */
    public int lineCnt;
    public MyFileException(int lineCnt){
        this.lineCnt = lineCnt;
    }
}
