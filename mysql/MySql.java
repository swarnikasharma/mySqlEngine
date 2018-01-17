/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package mysql;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author swarnika
 */
public class MySql {
    
    /**
     * @param args the command line arguments
     */
    static int selCount=0;
    static String table1="table1";
    static String table2="table2";
    static Vector select=new Vector();
    static Vector from=new Vector();
    static Vector where=new Vector();
    static Vector resultSetA=new Vector();
    static Vector resultSetB=new Vector();
    static Vector resultSetC=new Vector();
    static Vector resultSetD=new Vector();
    static Vector <Vector> resultantAttr =new Vector<Vector>();
    
    
    public static void executeInTable1()
    {
        try{
            
            FileInputStream fstream = new FileInputStream("table1.csv");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //process query
            //System.out.println("table1");
            String s;
            while((s=br.readLine())!=null)
            {
                String[] st=s.split(",");
                // System.out.println(s);
                resultSetA.addElement(st[0]);
                
                resultSetB.addElement(st[1]);
                
                resultSetC.addElement(st[2]);
            }
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void executeInTable2()
    {
        try{
            
            FileInputStream fstream = new FileInputStream("table2.csv");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //process query
            String s;
            while((s=br.readLine())!=null)
            {
                String[] st=s.split(",");
                // System.out.println(s);
                resultSetB.addElement(st[0]);
                
                resultSetD.addElement(st[1]);
                
            }
            
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void executeJoin()
    {
        try{
            
            FileInputStream fstream = new FileInputStream(table1);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //process query
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public static void parse(String query){
        StringTokenizer st=new StringTokenizer(query.toUpperCase()," ,");
        int state=0;//0=select;1=from;2=where
        while (st.hasMoreTokens()){
            String theToken=st.nextToken();
            //handles select clause cascading
            if(theToken.contains("("))
            {
                theToken=theToken.replace('(',' ');
            }
            if(theToken.contains(")"))
            {
                theToken=theToken.replace(')',' ');
            }
            System.out.println("Token:"+theToken);
            theToken=theToken.trim();
            if (theToken.equals("SELECT"))
            {
                state=0;
                selCount++;
                select.addElement(selCount);
                //  System.out.println("Token:"+theToken);
                
            }
            else if (theToken.equals("FROM"))
            {
                state=1;
                from.addElement(selCount);
                //System.out.println("Token:"+theToken);
                
            }
            else if (theToken.equals("WHERE"))
            {
                state=2;
                where.addElement(selCount);
                //System.out.println("Token:"+theToken);
                
                
            }
            else {
                if (state==0)select.addElement(theToken);
                if (state==1)from.addElement(theToken);
                if (state==2)where.addElement(theToken);
            }
        }
        //here the vectors select, from and where have what you are looking for
        System.out.println("NR of TOKEN IN SELECT:"+select.size());
        System.out.println("NR of TOKEN IN FROM:"+from.size());
        System.out.println("NR of TOKEN IN WHERE:"+where.size());
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length==0)
            System.out.println("No Query To Execute");
        //String query=args[1];
        //fetch the table name
        //if query contains a single table...call executeInTable function
        //else call executeJoin function
        String query="Select B,D from table2";
        parse(query);
        // System.out.println("contents");
        //executeInTable(table1);
        int fCount=selCount;
       // System.out.println(fCount);
        for(int i=fCount;i>0;i--)
        {
            int f=from.indexOf(i);
            //System.out.println(f);
            if(from.get(f+1).toString().equalsIgnoreCase(table1))
            {
                executeInTable1();
            }
            else if (from.get(f+1).toString().equalsIgnoreCase(table2))
            {
                executeInTable2();
            }
            else if((from.get(f+1).toString().equalsIgnoreCase(table1)&&f+2<from.size()&&from.get(f+2).toString().equalsIgnoreCase(table2))||(from.get(f+1).toString().equalsIgnoreCase(table2)&&f+2<from.size()&&from.get(f+2).toString().equalsIgnoreCase(table1)))
            {
                executeInTable1();
                executeInTable2();
                executeJoin();
            }
            //whereClause();
            selectClause();
            
        }
        
        
        
    }
    
    
    private static void selectClause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int s=select.indexOf(selCount);
        s++;
        selCount--;
        while(s<select.size()&&!Character.isDigit(select.get(s).toString().charAt(0)))
        {
            String attr=select.get(s).toString();
            
            if(attr.contains("min"))
            {
                if(attr.endsWith("A"))
                {
                    min(resultSetA);
                    resultantAttr.add(resultSetA);
                }
                else if(attr.endsWith("B"))
                {
                    min(resultSetB);
                    resultantAttr.add(resultSetB);
                }
                else if(attr.endsWith("C"))
                {
                    min(resultSetC);
                }
                else if(attr.endsWith("D"))
                {
                    min(resultSetD);
                }
                else
                {
                    System.out.println("Invalid query");
                }
            }
            else if(attr.contains("max"))
            {
                if(attr.endsWith("A"))
                {
                    max(resultSetA);
                }
                else if(attr.endsWith("B"))
                {
                    max(resultSetB);
                }
                else if(attr.endsWith("C"))
                {
                    max(resultSetC);
                }
                else if(attr.endsWith("D"))
                {
                    max(resultSetD);
                }
                else
                {
                    System.out.println("Invalid query");
                }
            }
            else if(attr.contains("sum"))
            {
                if(attr.endsWith("A"))
                {
                    sum(resultSetA);
                }
                else if(attr.endsWith("B"))
                {
                    sum(resultSetB);
                }
                else if(attr.endsWith("C"))
                {
                    sum(resultSetC);
                }
                else if(attr.endsWith("D"))
                {
                    sum(resultSetD);
                }
                else
                {
                    System.out.println("Invalid query");
                }
            }
            else if(attr.contains("avg"))
            {
                if(attr.endsWith("A"))
                {
                    avg(resultSetA);
                }
                else if(attr.endsWith("B"))
                {
                    avg(resultSetB);
                }
                else if(attr.endsWith("C"))
                {
                    avg(resultSetC);
                }
                else if(attr.endsWith("D"))
                {
                    avg(resultSetD);
                }
                else
                {
                    System.out.println("Invalid query");
                }
            }
            else
            {
                if(attr.contains("A"))
                {
                    resultantAttr.add(resultSetA);
                }
                else if(attr.contains("B"))
                {
                    resultantAttr.add(resultSetB);
                }
                else if(attr.endsWith("C"))
                {
                    resultantAttr.add(resultSetC);
                    
                }
                else if(attr.endsWith("D"))
                {
                    resultantAttr.add(resultSetD);
                }
                else
                {
                    System.out.println("Invalid query");
                }
            }
            s++;
        }
        //output the result stored in resultantAttr
        if(selCount==0)
        {
            for(int i=1;i<select.size()&&(!Character.isDigit(select.get(i).toString().charAt(0)));i++)
            {
                System.out.print(select.get(i).toString()+"       ");
            }
            System.out.println("");
            Vector inner = (Vector)resultantAttr.elementAt(0);
            for (int i = 0; i < inner.size(); i++)
            {
                //Vector inn = (Vector)resultantAttr.elementAt(i);
                for (int j = 0; j <resultantAttr.size(); j++)
                {
                    int m = Integer.parseInt(resultantAttr.get(j).get(i).toString());
                    System.out.print(m + "      ");
                }
                System.out.println();
            }
        }
        
    }
    private static void min(Vector v)
    {
        Collections.sort(v);
        String res;
        res = v.get(0).toString();
        v.clear();
        v.addElement(res);
    }
    private static void max(Vector v)
    {
        Collections.sort(v);
        String res;
        res = v.get(v.size()-1).toString();
        v.clear();
        v.addElement(res);
    }
    private static void sum(Vector v) {
        int s=0;
        for(int i=0;i<v.size();i++)
            s+=Integer.parseInt(v.get(i).toString());
        v.clear();
        v.addElement(s);
    }
    private static void avg(Vector v) {
        int n=v.size();
        sum(v);
        int ans=Integer.parseInt(v.get(0).toString());
        if(n!=0)
            ans=ans/n;
        v.clear();
        v.addElement(ans);
    }
    private static void whereClause()
    {
        
        int w=where.indexOf(selCount);
        w++;
        int n;
        while(w<where.size()&&Integer.parseInt(where.get(w).toString())!=selCount+1)
        {
            String a1=where.get(w).toString();
            w++;
            if(w+1<where.size()&&Character.isDigit(where.get(w+1).toString().charAt(0)))
            {
                n=Integer.parseInt(where.get(w+1).toString());
            }
        }
    }
}
