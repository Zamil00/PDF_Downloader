package downloader;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public class DownloadsTableModel extends AbstractTableModel implements Observer {

    private static final String[] columnNames = {"URL", "Size", "Progress", "Status"};
    private static final Class<?>[] columnClasses = {String.class, String.class, JProgressBar.class, String.class};
    private List<Download> downloadList = new ArrayList<>();

    public void addDownload(Download download) {
        download.addObserver(this);
        downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Download getDownload(int row) {
        return downloadList.get(row);
    }

    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return downloadList.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Download download = downloadList.get(row);
        switch (col) {
            case 0: // URL
                return download.getUrl();
            case 1: // Size
                int size = download.getSize();
                return (size == -1) ? "" : String.valueOf(size);
            case 2: // Progress
                return download.getProgress();
            case 3: // Status
                return getStatusString(download.getStatus());
        }
        return "";
    }

    private String getStatusString(int status) {
        switch (status) {
            case Download.DOWNLOADING:
                return "Downloading";
            case Download.PAUSED:
                return "Paused";
            case Download.COMPLETE:
                return "Complete";
            case Download.CANCELLED:
                return "Cancelled";
            case Download.ERROR:
                return "Error";
            default:
                return "";
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}
