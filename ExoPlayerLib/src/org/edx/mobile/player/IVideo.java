package org.openedu.www.player;

import java.util.List;

public interface IVideo {

    String getTitle();
    String getExternalLink();
    String getVideoLink();
    List<IClosedCaption> getClosedCaptions();
    void setSelectedClosedCaption(IClosedCaption lang);
    IClosedCaption getSelectedClosedCaption();

    public interface IClosedCaption {
        String getLanguage();
        String getPath();
    }

}
