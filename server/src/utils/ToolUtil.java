package utils;

import data.base.BaseNameVisibleData;
import data.base.BaseTool;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class ToolUtil {

    private static boolean hasFree(BaseTool baseTool) {
        return (baseTool.getCount() - baseTool.getInUse()) > 0;
    }

    private static boolean notUsed(BaseTool baseTool) {
        return baseTool.getInUse() == 0;
    }

    public static void hasFreeThrow(BaseTool baseTool) throws ControllerException {
        if (!hasFree(baseTool)) {
            throw new ControllerException("Přístroj '" + baseTool.getName() + "' je plně využíván");
        }
    }

    public static void increaseUsability(BaseTool baseTool) {
        if (hasFree(baseTool)) {
            baseTool.setInUse(baseTool.getInUse() + 1);
        }
    }

    public static void decreaseUsability(BaseTool baseTool) {
        if (!notUsed(baseTool)) {
            baseTool.setInUse(baseTool.getInUse() - 1);
        }
    }

    public static boolean isUse(BaseNameVisibleData source, BaseNameVisibleData... targets) {
        if (source == null) {
            return true;
        }
        for (BaseNameVisibleData item : targets) {
            if (item != null && item.getId().equals(source.getId())) {
                return true;
            }
        }
        return false;
    }
}
