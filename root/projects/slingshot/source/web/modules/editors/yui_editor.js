/**
 *  Adapter for YUI html editor (http://developer.yahoo.com/yui/editor/).
 * 
 */
Alfresco.util.RichEditorManager.addEditor('YAHOO.widget.SimpleEditor', function(id,config)
{
   var editor;
   return (
   {
      init: function RichEditorManager_yui_init(id,config)
      {
         editor = new YAHOO.widget.SimpleEditor(id, config);
         YAHOO.Bubbling.fire("editorInitialized", this);
         return this;
      },
      
      getEditor: function RichEditorManager_yui_getEditor()
      {
         return editor;
      },

      clear: function RichEditorManager_yui_clear()
      {
         editor.clearEditorDoc();
      },

      render: function RichEditorManager_yui_render()
      {
         editor.render();
      },

      disable: function RichEditorManager_yui_disable()
      {
         editor._disableEditor(true);
      },

      enable: function RichEditorManager_yui_enable()
      {
         editor._disableEditor(false);
      },

      getContent: function RichEditorManager_yui_getContent()
      { 
         return editor.getEditorHTML();
      }, 

      setContent: function RichEditorManager_yui_setContent(html)
      { 
         editor.setEditorHTML(html);
      },

      save: function RichEditorManager_yui_save()
      {
         editor.saveHTML();
      },

      getContainer: function RichEditorManager_yui_getContainer()
      {
         return editor.get('element_cont').get('element');
      },
      
      activateButton: function RichEditorManager_yui_activateButton(buttonId)
      {
         editor.toolbar.selectButton(buttonId);
      },
      
      deactivateButton: function RichEditorManager_yui_deactivateButton(buttonId)
      {
         editor.toolbar.deselectButton(buttonId);
      }
   });
});