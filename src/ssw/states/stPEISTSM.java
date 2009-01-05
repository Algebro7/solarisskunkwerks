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

import ssw.components.*;

public class stPEISTSM implements ifPhysEnhance, ifState {
    private final static AvailableCode AC = new AvailableCode( false, 'E', 'X', 'X', 'D',
        3050, 0, 0, "CC", "", false, false );
    private final MechModifier MechMod = new MechModifier( 1, 0, 0, 0.0f, 0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, true );

    public boolean IsClan() {
        return false;
    }
    
    public int GetTonnage( int mechtons ) {
        return 0;
    }
    
    public int GetCrits( int mechtons ) {
        return 6;
    }

    public String GetLookupName() {
        return "TSM";
    }

    public String GetCritName() {
        return "TSM";
    }

    public String GetMMName() {
        return "Triple Strength Myomer";
    }

    public boolean Contiguous() {
        return false;
    }

    public boolean CanArmor() {
        return false;
    }

    public float GetCost( int mechtons, float enginetons ) {
        return 16000 * mechtons;
    }
    
    public float GetOffensiveBV( int Tonnage ) {
        return Tonnage * 1.5f;
    }

    public float GetDefensiveBV( int Tonnage ) {
        return 0.0f;
    }

    public boolean IncrementPlaced() {
        return true;
    }

    public boolean DecrementPlaced() {
        return true;
    }

    public AvailableCode GetAvailability() {
        return AC;
    }
    
    public boolean IsCritable() {
        return false;
    }

    public MechModifier GetMechModifier() {
        return MechMod;
    }

    @Override
    public String toString() {
        return "TSM";
    }
}
