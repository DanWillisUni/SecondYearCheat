package question2;

public class StrategyFactory {
    public Strategy factory(String strat){
        switch (strat){
            case "Human":
                return (new HumanStrategy());
            case "Thinker":
                return(new ThinkerStrategy());
            case "My":
                return (new MyStrategy());
            default:
                return(new BasicStrategy());
        }
    }
}
