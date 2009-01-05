/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ssw.states;

import ssw.Constants;
import ssw.components.*;

public class stGyroISCompact implements ifGyro, ifState {
    private final static AvailableCode AC = new AvailableCode( false, 'E', 'X', 'X', 'E',
        3068, 0, 0, "FS", "", false, false );

    public stGyroISCompact() {
        AC.SetRulesLevelIM( Constants.UNALLOWED );
    }

    public boolean IsClan() {
        return false;
    }
    
    public float GetTonnage( int rating ) {
        return (float) (((int) ( rating * 0.01f + 0.99f )) * 1.5);
    }
    
    public int GetCrits() {
        return 2;
    }
    
    public String GetLookupName() {
        return "Compact Gyro";
    }

    public String GetCritName() {
        return "Compact Gyro";
    }

    public String GetMMName() {
        return "Gyro";
    }

    public String GetReportName() {
        return "Compact";
    }

    public float GetBVMult() {
        return 0.5f;
    }
    
    public float GetCostMult() {
        return 400000.0f;
    }
    
    public AvailableCode GetAvailability() {
        return AC;
    }
    
    public MechModifier GetMechModifier() {
        return null;
    }

    @Override
    public String toString() {
        return "Compact Gyro";
    }
}
