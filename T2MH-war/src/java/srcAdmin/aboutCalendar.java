package srcAdmin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class aboutCalendar {

    private String date;
    private Date maxDate;
    private String originalURL;
    private String baseURL;
    private String stringMaxDate;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Branch/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public String getStringMaxDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxDate = cal.getTime();
        stringMaxDate=format.format(maxDate);
        return stringMaxDate;
    }

    public void setStringMaxDate(String stringMaxDate) {
        this.stringMaxDate = stringMaxDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public String MaxDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxDate = cal.getTime();
        return format.format(maxDate);
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        this.date=format.format(event.getObject());
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected To Create", this.date));
    }

}
