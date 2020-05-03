package rendering;

import interfaces.IF_SceneItem;

import java.util.Comparator;

public class DistanceCompare implements Comparator<IF_SceneItem> {

    public int compare(IF_SceneItem _item1, IF_SceneItem _item2)
    {
        return Float.compare(_item2.getDistanceToCamera(), _item1.getDistanceToCamera());
    }
}
