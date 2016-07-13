package bn.blaszczyk.blstatistics.core;

import java.util.Comparator;

public class TeamResult
{
	public static final Comparator<TeamResult> COMPARATOR = new Comparator<TeamResult>(){
		@Override
		public int compare(TeamResult team1, TeamResult team2)
		{
			if(team1.getPoints() > team2.getPoints())
				return -1;
			if(team1.getPoints() < team2.getPoints())
				return 1;

			if(team1.getGoalDifference() > team2.getGoalDifference())
				return -1;
			if(team1.getGoalDifference() > team2.getGoalDifference())
				return 1;

			if(team1.getTeamGoals() > team2.getTeamGoals())
				return -1;
			if(team1.getTeamGoals() > team2.getTeamGoals())
				return 1;
			
			return 0;
		}
	};
	
	private String team;
	private int points=0;
	private int games=0;
	private int wins=0;
	private int draws=0;
	private int losses=0;
	private int teamGoals=0;
	private int opponentGoals=0;
	
	public TeamResult(String team)
	{
		this.team = team;
	}

	public String getTeam()
	{
		return team;
	}

	public int getPoints()
	{
		return points;
	}
	
	public int getGames()
	{
		return games;
	}

	public int getWins()
	{
		return wins;
	}

	public int getDraws()
	{
		return draws;
	}

	public int getLosses()
	{
		return losses;
	}

	public int getTeamGoals()
	{
		return teamGoals;
	}

	public int getOpponentGoals()
	{
		return opponentGoals;
	}
	
	public int getGoalDifference()
	{
		return teamGoals - opponentGoals;
	}
	
	public void consumeGame(Game game, int pointsForWin)
	{
		if(team.equals(game.getTeam1()))
		{
			switch(game.getResult())
			{
			case Game.WIN:
				wins++;
				points += pointsForWin;
				break;
			case Game.DRAW:
				draws++;
				points++;
				break;
			case Game.LOSS:
				losses++;
				break;
			}
			games ++;
			teamGoals += game.getGoals1();
			opponentGoals += game.getGoals2();
		}
		if(team.equals(game.getTeam2()))
		{
			switch(game.getResult())
			{
			case Game.LOSS:
				wins++;
				points += pointsForWin;
				break;
			case Game.DRAW:
				draws++;
				points++;
				break;
			case Game.WIN:
				losses++;
				break;
			}
			games++;
			teamGoals += game.getGoals2();
			opponentGoals += game.getGoals1();
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("%15s     %2d Spl   %2d Pkt  %3d Dif    %2dS %2dU %2dN    %3d:%3d", team, games, points, getGoalDifference(), wins, draws, losses, teamGoals, opponentGoals );
	}

}
