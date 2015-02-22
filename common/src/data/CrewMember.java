package data;

import action.base.ServerAction;
import data.base.BaseNameVisibleData;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class CrewMember extends BaseNameVisibleData {
    public CrewMember() {
        super("Člen posádky");
    }

    @Override
    public ServerAction.DataType getDataType() {
        return ServerAction.DataType.Crew;
    }
}
