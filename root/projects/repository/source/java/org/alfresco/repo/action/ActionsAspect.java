/*
 * Copyright (C) 2005-2007 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.repo.action;

import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.copy.CopyBehaviourCallback;
import org.alfresco.repo.copy.CopyDetails;
import org.alfresco.repo.copy.CopyServicePolicies;
import org.alfresco.repo.copy.DefaultCopyBehaviourCallback;
import org.alfresco.repo.policy.BehaviourFilter;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.rule.RuleService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.surf.util.PropertyCheck;

/**
 * Class containing behaviour for the actions aspect
 * 
 * @author Roy Wetherall
 */
public class ActionsAspect implements CopyServicePolicies.OnCopyNodePolicy, CopyServicePolicies.OnCopyCompletePolicy
{
    private PolicyComponent policyComponent;
    private BehaviourFilter behaviourFilter;
    private RuleService ruleService;
    private NodeService nodeService;
    
    public void setPolicyComponent(PolicyComponent policyComponent)
    {
        this.policyComponent = policyComponent;
    }
    
    public void setBehaviourFilter(BehaviourFilter behaviourFilter)
    {
        this.behaviourFilter = behaviourFilter;
    }

    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }
   
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }
    
    public void init()
    {
        PropertyCheck.mandatory(this, "policyComponent", policyComponent);
        PropertyCheck.mandatory(this, "behaviourFilter", behaviourFilter);
        PropertyCheck.mandatory(this, "ruleService", ruleService);
        PropertyCheck.mandatory(this, "nodeService", nodeService);
        
        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, "getCopyCallback"),
                ActionModel.ASPECT_ACTIONS,
                new JavaBehaviour(this, "getCopyCallback"));
        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, "onCopyComplete"),
                ActionModel.ASPECT_ACTIONS,
                new JavaBehaviour(this, "onCopyComplete"));
        
        this.policyComponent.bindClassBehaviour(
                QName.createQName(NamespaceService.ALFRESCO_URI, "onAddAspect"), 
                ActionModel.ASPECT_ACTIONS, 
                new JavaBehaviour(this, "onAddAspect"));
    }
    
    /**
     * On add aspect policy behaviour
     * 
     * @param nodeRef
     * @param aspectTypeQName
     */
    public void onAddAspect(NodeRef nodeRef, QName aspectTypeQName)
    {
        this.ruleService.disableRules(nodeRef);
        try
        {
            this.nodeService.createNode(
                    nodeRef,
                    ActionModel.ASSOC_ACTION_FOLDER,
                    ActionModel.ASSOC_ACTION_FOLDER,
                    ContentModel.TYPE_SYSTEM_FOLDER);
        }
        finally
        {
            this.ruleService.enableRules(nodeRef);
        }
    }
    
    /**
     * @return              Returns {@link ActionsAspectCopyBehaviourCallback}
     */
    public CopyBehaviourCallback getCopyCallback(QName classRef, CopyDetails copyDetails)
    {
        return new ActionsAspectCopyBehaviourCallback(behaviourFilter);
    }
    
    /**
     * Extends the default copy behaviour to include cascading to action folders.
     * 
     * @author Derek Hulley
     * @since 3.2
     */
    private static class ActionsAspectCopyBehaviourCallback extends DefaultCopyBehaviourCallback
    {
        private final BehaviourFilter behaviourFilter;
        private ActionsAspectCopyBehaviourCallback(BehaviourFilter behaviourFilter)
        {
            this.behaviourFilter = behaviourFilter;
        }

        /**
         * Disables the aspect behaviour for this node
         * 
         * @return          Returns <tt>true</tt>
         */
        @Override
        public boolean getMustCopy(QName classQName, CopyDetails copyDetails)
        {
            NodeRef targetNodeRef = copyDetails.getTargetNodeRef();
            behaviourFilter.disableBehaviour(targetNodeRef, ActionModel.ASPECT_ACTIONS);
            // Always copy
            return true;
        }

        /**
         * Always cascades to the action folders
         */
        @Override
        public ChildAssocCopyAction getChildAssociationCopyAction(
                QName classQName,
                CopyDetails copyDetails,
                CopyChildAssociationDetails childAssocCopyDetails)
        {
            ChildAssociationRef childAssocRef = childAssocCopyDetails.getChildAssocRef();
            if (childAssocRef.getTypeQName().equals(ActionModel.ASSOC_ACTION_FOLDER))
            {
                return ChildAssocCopyAction.COPY_CHILD;
            }
            else
            {
                throw new IllegalStateException(
                        "Behaviour should have been invoked: \n" +
                        "   Aspect: " + this.getClass().getName() + "\n" +
                        "   " + childAssocCopyDetails + "\n" +
                        "   " + copyDetails);
            }
        }
    }

    /**
     * Re-enable aspect behaviour for the source node
     */
    public void onCopyComplete(
            QName classRef,
            NodeRef sourceNodeRef,
            NodeRef destinationRef,
            boolean copyToNewNode,
            Map<NodeRef, NodeRef> copyMap)
    {
        behaviourFilter.enableBehaviour(sourceNodeRef, ActionModel.ASPECT_ACTIONS);
    }
}
