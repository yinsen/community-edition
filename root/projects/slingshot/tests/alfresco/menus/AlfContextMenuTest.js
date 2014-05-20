/**
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This is the unit test for the alfresco/menus/AlfMenuItemWrapper widget.
 * 
 * @author Dave Draper
 */
define(["intern!object",
        "intern/chai!assert",
        "require",
        "alfresco/TestCommon",
        "intern/dojo/node!wd/lib/special-keys"], 
        function (registerSuite, assert, require, TestCommon, specialKeys) {

   registerSuite({
      name: 'AlfContextMenu Test',
      'alfresco/menus/AlfContextMenu': function () {

         var browser = this.remote;
         return TestCommon.bootstrapTest(this.remote, "./tests/alfresco/menus/page_models/AlfContextMenu_TestPage.json")

            // TESTS COMMENTED OUT PENDING ANSWER POSTED HERE: https://github.com/theintern/intern/issues/191
            // .elementByCss("#LOGO")
            //    .moveTo()
            //    .click("2")
            //    .end()
            // .elementByCss("#MI3")
            //    .moveTo()
            //    .click()
            //    .end()
            // .elementsByCss(TestCommon.pubSubDataCssSelector("last", "key3", "value3"))
            //    .then(function(elements) {
            //       TestCommon.log(testname,50,"Check targeted node context menu works");
            //       assert(elements.length == 1, "Test #1 - Targeted node context menu failure");
            //    })
            //    .end()

            // .elementByCss("#CLASSIC_WINDOW div.content")
            //    .moveTo()
            //    .click("2")
            //    .end()
            // .elementByCss("#MI1")
            //    .moveTo()
            //    .click()
            //    .end()
            // .elementsByCss(TestCommon.pubSubDataCssSelector("last", "key1", "value1"))
            //    .then(function(elements) {
            //       TestCommon.log(testname,50,"Check inherited node context menu works");
            //       assert(elements.length == 1, "Test #2 - Inherited node context menu failure");
            //    })
            //    .end()

            // Post the coverage results...
            .then(function() {
               TestCommon.postCoverageResults(browser);
            })
            .end();
      }
   });
});