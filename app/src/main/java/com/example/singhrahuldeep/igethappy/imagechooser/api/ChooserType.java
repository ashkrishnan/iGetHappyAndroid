/*******************************************************************************
 * Copyright 2013 Kumar Bibek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    
 * http://www.apache.org/licenses/LICENSE-2.0
 * 	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.singhrahuldeep.igethappy.imagechooser.api;

public interface ChooserType {
    int REQUEST_PICK_PICTURE = 291;
    
    int REQUEST_CAPTURE_PICTURE = 294;
    
    int REQUEST_CAPTURE_VIDEO = 292;
    
    int REQUEST_PICK_VIDEO = 295;
    
    int REQUEST_PICK_PICTURE_OR_VIDEO = 300;

    int REQUEST_PICK_FILE = 500;
}
