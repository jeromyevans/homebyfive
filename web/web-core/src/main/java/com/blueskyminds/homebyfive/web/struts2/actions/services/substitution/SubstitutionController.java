package com.blueskyminds.homebyfive.web.struts2.actions.services.substitution;

import org.apache.struts2.config.Results;
import org.apache.struts2.config.Result;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import org.apache.commons.lang.StringUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.Substitution;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.web.struts2.actions.services.substitution.SubstitutionTableModelFactory;
import com.wideplay.warp.persist.Transactional;
import com.google.inject.Inject;

import java.util.List;

/**
 * Date Started: 3/05/2008
 */
@Results({
    @Result(name = "index", type = FreemarkerResult.class, value = "/results/datatable.ftl"),
    @Result(name = "create", value = ""),
    @Result(name = "delete", value = ""),
    @Result(name = "update", value = "")}
)
public class SubstitutionController extends ActionSupport implements ModelDriven<Object>, Preparable {

    private SubstitutionService substitutionService;

    private Substitution substitution;
    private TableModel substitutions;
    private Long id;
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }                                                         

    public void prepare() throws Exception {
    }

    public HttpHeaders index() {
        List<Substitution> substitutionList;
        if (StringUtils.isNotBlank(group)) {
            substitutionList = substitutionService.listSubstitutionsForGroup(group);
        } else {
            substitutionList = substitutionService.listSubstitutions();
        }

        substitutions = SubstitutionTableModelFactory.createTable(substitutionList);

        return new DefaultHttpHeaders("index").disableCaching();
    }

    public void prepareCreate() {
        substitution = new Substitution();
    }

    /**
     * Create a new substitution
     *
     * @return
     */
    @Transactional
    public HttpHeaders create() {
        if (StringUtils.isNotBlank(substitution.getPattern())) {
            substitution.setGroupName(group);
            substitution.setGroupNo(0);
            substitution.setExclusive(true);
            substitution = substitutionService.createSubstitution(substitution);
        }

        // and return created with location id
        return new DefaultHttpHeaders("create");
    }

    @Transactional
    public HttpHeaders destroy() {
        if (id != null) {
            substitutionService.deleteSubstitution(id);
        }
        // and return created with location id
        return new DefaultHttpHeaders("delete");
    }

    public void prepareUpdate() {
        substitution = new Substitution();
    }

    @Transactional
    public HttpHeaders update() {
        if (id != null) {
            substitutionService.updateSubstitution(id, substitution);
        }
        return new DefaultHttpHeaders("update").setLocationId(id);
    }

    public Object getModel() {
        return substitutions != null ? substitutions : substitution;
    }

    public void setModel(Substitution substitution) {
        this.substitution = substitution;
    }

    public void setId(Long id) {
        this.id = id;
    }
        
    @Inject
    public void setSubstitutionService(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }
}
