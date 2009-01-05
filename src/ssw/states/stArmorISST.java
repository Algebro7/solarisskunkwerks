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

public class stArmorISST implements ifArmor, ifState {
    private final static AvailableCode AC = new AvailableCode( false, 'E', 'X', 'X', 'E',
        3063, 0, 0, "CC", "", false, false );
    private final MechModifier MechMod = new MechModifier( 0, 0, 0, 0.0f, 0, 0, 10, 0.2f, 0.0f, 0.0f, 0.0f, true );

    public stArmorISST() {
        AC.SetRulesLevelIM( Constants.UNALLOWED );
    }

    public String GetLookupName() {
        return "Stealth Armor";
    }

    public String GetCritName() {
        return "Stealth Armor";
    }

    public String GetMMName() {
        return "Stealth Armor";
    }

    public boolean IsClan() {
        return false;
    }

    public boolean Place( Armor a, ifLoadout l ) {
        // Place the armor in the mech
        boolean placed = false;
        int increment = 11;
        try {
            if( l.IsQuad() ) {
                // these crits can only ever go here, no need to check.
                l.AddToLA( a, 4 );
                l.AddToLA( a, 5 );
                l.AddToRA( a, 4 );
                l.AddToRA( a, 5 );
            } else {
                // check each available space from the bottom.  If we cannot
                // allocate then we need to revert to normal armor
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToLA( a, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
                placed = false;
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToLA( a, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
                placed = false;
                increment = 11;
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToRA( a, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
                placed = false;
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToRA( a, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
            }

            // check each available space from the bottom.  If we cannot allocate
            // then we need to revert to normal armor
            placed = false;
            increment = 11;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToLT( a, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }
            placed = false;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToLT( a, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }
            placed = false;
            increment = 11;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToRT( a, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }
            placed = false;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToRT( a, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }

            // leg crits can only ever go here, so no need to check.
            l.AddToLL( a, 4 );
            l.AddToLL( a, 5 );
            l.AddToRL( a, 4 );
            l.AddToRL( a, 5 );
        } catch ( Exception e ) {
            // something else was probably in the way.  Tell the placer
            return false;
        }

        // all went well
        return true;
    }

    public boolean Place( Armor a, ifLoadout l, LocationIndex[] Locs ) {
        LocationIndex li;
        try {
            for( int i = 0; i < Locs.length; i++ ) {
                li = (LocationIndex) Locs[i];
                l.AddTo( a, li.Location, li.Index );
            }
        } catch( Exception e ) {
            return false;
        }
        return true;
    }

    public int NumCrits() {
        return 12;
    }

    public float GetAVMult() {
        return 1.0f;
    }

    public boolean IsStealth() {
        return true;
    }

    public float GetCostMult() {
        return 50000.0f;
    }

    public float GetBVTypeMult() {
        return 1.0f;
    }

    public boolean LocationLocked() {
        // stealth armor is always locked.
        return true;
    }

    public void SetLocked( boolean l ) {
        // stealth armor is always locked.
    }

    public MechModifier GetMechModifier() {
        return MechMod;
    }

    public AvailableCode GetAvailability() {
        return AC;
    }
}
