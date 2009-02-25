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

public class stPECLMASC implements ifPhysEnhance, ifState {
    private final static AvailableCode AC = new AvailableCode( true, 'E', 'X', 'C', 'D',
        2740, 0, 0, "TH", "", false, false );
    private final MechModifier MechMod = new MechModifier( 0, 0, 0, 0.5f, 0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, true );

    public stPECLMASC() {
        AC.SetRulesLevelIM( Constants.UNALLOWED );
    }

    public boolean IsClan() {
        return true;
    }
    
    public int GetTonnage( int mechtons ) {
        return (int) (mechtons * 0.04f + 0.51f);
    }
    
    public int GetCrits( int mechtons ) {
        return (int) (mechtons * 0.04f + 0.51f);
    }

    public String GetLookupName() {
        return "MASC";
    }

    public String GetCritName() {
        return "MASC";
    }

    public String GetMMName() {
        return "CLMASC";
    }

    public boolean Contiguous() {
        return true;
    }

    public boolean CanArmor() {
        return true;
    }

    public float GetCost( int mechtons, float enginetons ) {
        return 1000 * enginetons * GetTonnage( mechtons );
    }

    public float GetOffensiveBV( int Tonnage ) {
        return 0.0f;
    }

    public float GetDefensiveBV( int Tonnage ) {
        return 0.0f;
    }

    public boolean IncrementPlaced() {
        return false;
    }

    public boolean DecrementPlaced() {
        return false;
    }

    public AvailableCode GetAvailability() {
        return AC;
    }
    
    public boolean IsCritable() {
        return true;
    }

    public MechModifier GetMechModifier() {
        return MechMod;
    }

    public Exclusion GetExclusions() {
        return new Exclusion( new String[] { "A.E.S." }, "MASC" );
    }

    @Override
    public String toString() {
        return "MASC";
    }
}
