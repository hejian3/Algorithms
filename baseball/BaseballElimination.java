import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hejian on 2018/4/29.
 */
public class BaseballElimination {

    private final int numberOfTeams;
    private final List<String> teamList;
    private final int []w;
    private final int []lost;
    private final int []left;
    private final int [][]g;
    private final Hashtable<String,Integer> teamMap;
    private final Hashtable<String,Queue<String>> set;

    /**
     * create a baseball division from given filename in format specified below
     * @param filename
     */
    public BaseballElimination(String filename){
        In in  = new In(filename);
        numberOfTeams = in.readInt();
        set = new Hashtable<>();
        teamList = new ArrayList<>(numberOfTeams);
        teamMap = new Hashtable<>(numberOfTeams);
        w = new int[numberOfTeams];
        lost = new int[numberOfTeams];
        left = new int[numberOfTeams];
        g    = new int[numberOfTeams][numberOfTeams];
        for(int i = 0; i < numberOfTeams; i++){
            String team = in.readString();
            teamList.add(team);
            teamMap.put(team,i);
            w[i] = in.readInt();
            lost[i] = in.readInt();
            left[i] = in.readInt();
            for(int j = 0; j < numberOfTeams; j++) {
                g[i][j] = in.readInt();
            }
        }
        setupEliminationSet();
    }

    private void setupEliminationSet(){
        int s = 0;
        int vmin = getTeamV(0,0);
        int vmax = vmin + numberOfTeams - 2;
        int t = vmax + 1;

        for(int i = 0; i < numberOfTeams; i++){
            int maxWin = w[i] + left[i];

            for (int j = 0; j < numberOfTeams; j++) {
                if (j != i && maxWin < w[j]) {
                    if(!set.containsKey(teamList.get(i)))set.put(teamList.get(i),new Queue<>());
                    set.get(teamList.get(i)).enqueue(teamList.get(j));
                }
            }

            FlowNetwork flowNetwork = getFlowNetwork(s, t,i);
            String [] tNameArr = new String[numberOfTeams -1];
            int count =0;
            for (int j = 0; j < numberOfTeams; j++) {
                if(j !=i){
                    double wLeft = maxWin - w[j];
                    wLeft = wLeft < 0 ? left[j]:wLeft;
                    int v = getTeamV(j,i);
                    tNameArr[count] = teamList.get(j);
                    count++;
                    FlowEdge flowEdge = new FlowEdge(v,t,wLeft);
                    flowNetwork.addEdge(flowEdge);
                }
            }
            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork,s, t);
            for(int l = vmin; l <= vmax ;l++){
                if(fordFulkerson.inCut(l)){
                    if(!set.containsKey(teamList.get(i)))set.put(teamList.get(i),new Queue<>());
                    set.get(teamList.get(i)).enqueue(tNameArr[l-vmin]);
                }
            }
        }
    }

    private FlowNetwork getFlowNetwork(int s, int t,int v){
        FlowNetwork flowNetwork = new FlowNetwork(t+1);
        int count = 0;
        for(int i = 0; i < numberOfTeams; i++){
            if(i == v)continue;
            for (int j =0; j < numberOfTeams; j++){
                if(j <= i || j == v)continue;
                int v1 = getTeamV(i,v);
                int v2 = getTeamV(j,v);
                count++;
                if(count > t)break;
                FlowEdge flowEdge1 = new FlowEdge(s,count,g[i][j]);
                flowNetwork.addEdge(flowEdge1);
                FlowEdge flowEdge2 = new FlowEdge(count,v1,Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge2);
                FlowEdge flowEdge3 = new FlowEdge(count,v2,Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge3);
            }
        }
        return flowNetwork;
    }

    private int getTeamV(int i,int v){
        int result = (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + i + 1;
        if(i > v) {
            result -=1;
        }
        return result;
    }

    /**
     * number of teams
     * @return
     */
    public int numberOfTeams(){
        return numberOfTeams;
    }

    /**
     * all teams
     * @return
     */
    public Iterable<String> teams(){
        return teamList;
    }

    /**
     * number of wins for given team
     * @param team
     * @return
     */
    public int wins(String team){
        checkTeamName(team);
        int index = teamMap.get(team);
        return w[index];
    }

    /**
     * number of losses for given team
     * @param team
     * @return
     */
    public int losses(String team){
        checkTeamName(team);
        int index = teamMap.get(team);
        return lost[index];
    }

    /**
     * number of remaining games for given team
     * @param team
     * @return
     */
    public int remaining(String team){
        checkTeamName(team);
        int index = teamMap.get(team);
        return left[index];
    }

    /**
     * number of remaining games between team1 and team2
     * @param team1
     * @param team2
     * @return
     */
    public int against(String team1,String team2){
        checkTeamName(team1);
        checkTeamName(team2);
        int index1 = teamMap.get(team1);
        int index2 = teamMap.get(team2);
        return g[index1][index2];
    }

    /**
     * is given team eliminated?
     * @param team
     * @return
     */
    public boolean isEliminated(String team){
        checkTeamName(team);
        return set.containsKey(team);
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated
     * @param team
     * @return
     */
    public Iterable<String> certificateOfElimination(String team){
        checkTeamName(team);
        return set.get(team);
    }

    private void checkTeamName(String team){
        if(!teamMap.containsKey(team))
            throw new java.lang.IllegalArgumentException("invalid team");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("C:\\Users\\hejian\\Desktop\\baseball-testing\\baseball\\teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

