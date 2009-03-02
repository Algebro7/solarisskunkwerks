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

package ssw.components;

import ssw.*;
import ssw.states.*;

public class Armor  extends abPlaceable {
    // the armor of the mech

    // Declares
    private Mech Owner;
    private int Placed = 0;
    private int[] ArmorPoints = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int[] MaxArmor = { 0, 0, 0, 0, 0, 0, 0, 0 };
    private ifArmor ISIN = new stArmorISIN(),
                    ISMS = new stArmorISMS(),
                    ISFF = new stArmorISFF(),
                    ISST = new stArmorISST(),
                    ISLF = new stArmorISLF(),
                    ISHF = new stArmorISHF(),
                    ISHA = new stArmorISHA(),
                    ISLR = new stArmorISLR(),
                    ISRE = new stArmorISRE(),
                    ISCM = new stArmorISCM(),
                    CLIN = new stArmorCLIN(),
                    CLMS = new stArmorCLMS(),
                    CLFF = new stArmorCLFF(),
                    CLFL = new stArmorCLFL(),
                    CLHA = new stArmorCLHA(),
                    CLLR = new stArmorCLLR(),
                    CLRE = new stArmorCLRE(),
                    CLCM = new stArmorCLCM();
    private ifArmor Config = ISMS;

    public Armor( Mech m ) {
        Owner = m;
        SetMaxArmor();
    }

    public void SetISIN() {
        // set the armor to Inner Sphere Industrial
        Config = ISIN;
    }

    public void SetISMS() {
        // set the armor to Inner Sphere Military Standard
        Config = ISMS;
    }

    public void SetISFF() {
        // set the armor to Inner Sphere Ferro-Fibrous
        Config = ISFF;
    }

    public void SetISST() {
        // set the armor to Inner Sphere Stealth
        Config = ISST;
    }

    public void SetISLF() {
        // set the armor to Inner Sphere Light Ferro-Fibrous
        Config = ISLF;
    }

    public void SetISHF() {
        // set the armor to Inner Sphere Heavy Ferro-Fibrous
        Config = ISHF;
    }

    public void SetISHA() {
        // set the armor to Inner Sphere Hardened
        Config = ISHA;
    }

    public void SetISLR() {
        // set the armor to Inner Sphere Laser-Reflective
        Config = ISLR;
    }

    public void SetISRE() {
        // set the armor to Inner Sphere Reactive
        Config = ISRE;
    }

    public void SetISCM() {
        Config = ISCM;
    }

    public void SetCLIN() {
        // sets the armor to Clan Industrial
        Config = CLIN;
    }

    public void SetCLMS() {
        // set the armor to Clan Military Standard
        Config = CLMS;
    }

    public void SetCLFF() {
        // set the armor to Clan Ferro-Fibrous
        Config = CLFF;
    }

    public void SetCLFL() {
        // set the armor to Clan Ferro-Lamellor
        Config = CLFL;
    }

    public void SetCLHA() {
        // set the armor to Clan Hardened
        Config = CLHA;
    }

    public void SetCLLR() {
        // set the armor to Inner Sphere Laser-Reflective
        Config = CLLR;
    }

    public void SetCLRE() {
        // set the armor to Inner Sphere Reactive
        Config = CLRE;
    }

    public void SetCLCM() {
        Config = CLCM;
    }

    public void Recalculate() {
        // recalculates the armor if mech tonnage or motive type changes
        SetMaxArmor();

        // now that we've set the maximums, make sure we're not exceeding them
        // the head max never changes during a recalc so we'll ignore it
        if( ArmorPoints[Constants.LOC_CT] + ArmorPoints[Constants.LOC_CTR] > MaxArmor[Constants.LOC_CT] ) {
            int rear = Math.round( MaxArmor[Constants.LOC_CT] * Owner.GetOptions().Armor_CTRPercent / 100 );
            SetArmor( Constants.LOC_CTR, rear );
            SetArmor( Constants.LOC_CT, MaxArmor[Constants.LOC_CT] - rear );
        }
        if( ArmorPoints[Constants.LOC_LT] + ArmorPoints[Constants.LOC_LTR] > MaxArmor[Constants.LOC_LT] ) {
            int rear = Math.round( MaxArmor[Constants.LOC_LT] * Owner.GetOptions().Armor_STRPercent / 100 );
            SetArmor( Constants.LOC_LTR, rear );
            SetArmor( Constants.LOC_LT, MaxArmor[Constants.LOC_LT] - rear );
        }
        if( ArmorPoints[Constants.LOC_RT] + ArmorPoints[Constants.LOC_RTR] > MaxArmor[Constants.LOC_RT] ) {
            int rear = Math.round( MaxArmor[Constants.LOC_RT] * Owner.GetOptions().Armor_STRPercent / 100 );
            SetArmor( Constants.LOC_RTR, rear );
            SetArmor( Constants.LOC_RT, ( MaxArmor[Constants.LOC_RT] - rear ) );
        }
        if( ArmorPoints[Constants.LOC_LA] > MaxArmor[Constants.LOC_LA] ) {
            SetArmor( Constants.LOC_LA, MaxArmor[Constants.LOC_LA] );
        }
        if( ArmorPoints[Constants.LOC_RA] > MaxArmor[Constants.LOC_RA] ) {
            SetArmor( Constants.LOC_RA, MaxArmor[Constants.LOC_RA] );
        }
        if( ArmorPoints[Constants.LOC_LL] > MaxArmor[Constants.LOC_LL] ) {
            SetArmor( Constants.LOC_LL, MaxArmor[Constants.LOC_LL] );
        }
        if( ArmorPoints[Constants.LOC_RL] > MaxArmor[Constants.LOC_RL] ) {
            SetArmor( Constants.LOC_RL, MaxArmor[Constants.LOC_RL] );
        }
    }

    public void SetMaxArmor() {
        // this sets the maximum array when tonnage changes.
        InternalStructure IntStruc = Owner.GetIntStruc();

        MaxArmor[Constants.LOC_HD] = 9;
        MaxArmor[Constants.LOC_CT] = IntStruc.GetCTPoints() * 2;
        MaxArmor[Constants.LOC_LT] = IntStruc.GetSidePoints() * 2;
        MaxArmor[Constants.LOC_RT] = IntStruc.GetSidePoints() * 2;
        MaxArmor[Constants.LOC_LA] = IntStruc.GetArmPoints() * 2;
        MaxArmor[Constants.LOC_RA] = IntStruc.GetArmPoints() * 2;
        MaxArmor[Constants.LOC_LL] = IntStruc.GetLegPoints() * 2;
        MaxArmor[Constants.LOC_RL] = IntStruc.GetLegPoints() * 2;
    }

    public void IncrementArmor( int Loc ) {
        // Check the location and see what we have to do
        switch( Loc ) {
            case 0: case 4: case 5: case 6: case 7:
                IncrementSingle( Loc );
                break;
            case 1: case 2: case 3: case 8: case 9: case 10:
                // now we have to figure out the opposite side.
                int rear = Loc;
                if( Loc < 8 ) {
                    rear += 7;
                } else {
                    rear -= 7;
                }
                IncrementDouble( Loc, rear );
                break;
        }
    }

    private void IncrementSingle( int Loc ) {
        // Make sure we're not exceeding the max
        if( ArmorPoints[Loc] < MaxArmor[Loc] ) {
            ArmorPoints[Loc]++;
        }
    }

    private void IncrementDouble( int LocFront, int LocRear ) {
        int MaxLoc = 0;
        // figure out which one is the actual front, since they're nebulously passed in.
        if( LocFront > LocRear ) {
            // They're reversed
            MaxLoc = LocRear;
        } else {
            // not reversed.
            MaxLoc = LocFront;
        }

        // Make sure we're not exceeding the max
        if( ArmorPoints[LocFront] < MaxArmor[MaxLoc] ) {
            ArmorPoints[LocFront]++;
            // now check to see if the rear needs to be decremented
            if( ArmorPoints[LocFront] + ArmorPoints[LocRear] > MaxArmor[MaxLoc] ) {
                if( MaxArmor[MaxLoc] == ArmorPoints[LocFront] ) {
                    ArmorPoints[LocRear] = 0;
                } else {
                    // Should check and see how far above we are, just in case
                    int dif = ArmorPoints[LocFront] + ArmorPoints[LocRear] - MaxArmor[MaxLoc];
                    while( dif > 0 ) {
                        // makes sure we're not decrementing it below 0 without extra code
                        DecrementArmor( LocRear );
                        dif--;
                    }
                }
            }
        }
    }

    public void DecrementArmor( int Loc ) {
        // Make sure we're not going below 0
        if( ArmorPoints[Loc] <= 0 ) {
            ArmorPoints[Loc] = 0;
        } else {
            ArmorPoints[Loc]--;
        }
    }

    public void SetArmor( int Loc, int av ) {
        // Check the location and see what we have to do
        switch( Loc ) {
            case 0: case 4: case 5: case 6: case 7:
                SetSingle( Loc, av );
                break;
            case 1: case 2: case 3: case 8: case 9: case 10:
                // now we have to figure out the opposite side.
                int rear = Loc;
                if( Loc < 8 ) {
                    rear += 7;
                } else {
                    rear -= 7;
                }
                SetDouble( Loc, rear, av );
                break;
        }
       
    }

    private void SetSingle( int Loc, int av ) {
        // make sure we're within bounds
        if( av > MaxArmor[Loc] ) {
            ArmorPoints[Loc] = MaxArmor[Loc];
        } else if( av < 0 ) {
            ArmorPoints[Loc] = 0;
        } else {
            ArmorPoints[Loc] = av;
        }
    }

    private void SetDouble( int LocFront, int LocRear, int av ) {
        int MaxLoc = 0;
        // figure out which one is the actual front, since they're nebulously
        // passed in and we could get some array out of bounds errors
        if( LocFront > LocRear ) {
            // They're reversed
            MaxLoc = LocRear;
        } else {
            // not reversed.
            MaxLoc = LocFront;
        }
        
        // make sure we're within bounds
        if( av > MaxArmor[MaxLoc] ) {
            ArmorPoints[LocFront] = MaxArmor[MaxLoc];
            // also need to fix the rear to the least
            ArmorPoints[LocRear] = 0;
        } else if( av < 0 ) {
            ArmorPoints[LocFront] = 0;
        } else {
            // make sure we decrement the rear a similar amount if needed.
            if( av + ArmorPoints[LocRear] > MaxArmor[MaxLoc] ) {
                if( MaxArmor[MaxLoc] == av ) {
                    ArmorPoints[LocRear] = 0;
                } else {
                    // find the difference
                    int dif = av + ArmorPoints[LocRear] - MaxArmor[MaxLoc];
                    while( dif > 0 ) {
                        // makes sure we're not decrementing it below 0 without extra code
                        DecrementArmor( LocRear );
                        dif--;
                    }
                }
            }
            // finally, set the armor
            ArmorPoints[LocFront] = av;
        }
    }

    public int GetLocationArmor( int Loc ) {
        return ArmorPoints[Loc];
    }

    public int GetLocationMax( int Loc ) {
        return MaxArmor[Loc];
    }

    public int GetMaxArmor() {
        // returns the maximum amount of armor allowed.\
        int result = 0;
        result += MaxArmor[0];
        result += MaxArmor[1];
        result += MaxArmor[2];
        result += MaxArmor[3];
        result += MaxArmor[4];
        result += MaxArmor[5];
        result += MaxArmor[6];
        result += MaxArmor[7];
        return result;
    }

    public int GetArmorValue() {
        int result = 0;
        result += ArmorPoints[0];
        result += ArmorPoints[1];
        result += ArmorPoints[2];
        result += ArmorPoints[3];
        result += ArmorPoints[4];
        result += ArmorPoints[5];
        result += ArmorPoints[6];
        result += ArmorPoints[7];
        result += ArmorPoints[8];
        result += ArmorPoints[9];
        result += ArmorPoints[10];
        return result;
    }

    public int GetModularArmorValue() {
        int result = 0;
        int[] ModArmor = Owner.GetLoadout().FindModularArmor();
        result += ModArmor[0] * 10;
        result += ModArmor[1] * 10;
        result += ModArmor[2] * 10;
        result += ModArmor[3] * 10;
        result += ModArmor[4] * 10;
        result += ModArmor[5] * 10;
        result += ModArmor[6] * 10;
        result += ModArmor[7] * 10;
        result += ModArmor[8] * 10;
        result += ModArmor[9] * 10;
        result += ModArmor[10] * 10;
        return result;
    }

    public boolean IsClan() {
        return ((ifState) Config).IsClan();
    }

    public boolean IsStealth() {
        return Config.IsStealth();
    }

    @Override
    public boolean Place( ifLoadout l ) {
        return Config.Place( this, Owner.GetLoadout() );
    }

    @Override
    public boolean Place( ifLoadout l, LocationIndex[] a ) {
        return Config.Place( this, l, a );
    }

    @Override
    public boolean CanArmor() {
        // armor is always roll again, so no armoring
        return false;
    }

    @Override
    public String GetCritName() {
        return Config.GetCritName();
    }

    public String GetMMName( boolean UseRear ) {
        return Config.GetMMName();
    }

    public String GetLookupName() {
        return ((ifState) Config).GetLookupName();
    }

    @Override
    public int NumCrits() {
        return Config.NumCrits();
    }

    @Override
    public int NumPlaced() {
        return Placed;
    }

    @Override
    public void IncrementPlaced() {
        Placed++;
    }

    @Override
    public void DecrementPlaced() {
        Placed--;
    }

    @Override
    public float GetTonnage() {
        // this has to return the nearest half-ton.
        float result = GetArmorValue() / ( 8 * Config.GetAVMult() );
        int mid = (int) Math.floor( result + 0.9999f );
        result = mid * 0.5f;
        return result;
    }

    public float GetWastedTonnage() {
        // returns the amount of tonnage wasted due to unspent armor points
        float result = GetTonnage() - GetArmorValue() / ( 16 * Config.GetAVMult() );
        if( result < 0.0f ) { result = 0.0f; }
        return (float) Math.floor( result * 100 ) / 100;
    }

    public int GetWastedAV() {
        // returns the amount of armor points left in the current half-ton lot
        // get the amount of wasted tonnage
        float Waste = 0.5f - ( GetTonnage() - GetArmorValue() / ( 16 * Config.GetAVMult() ) );
        int result = (int) Math.floor( ( 8 * Config.GetAVMult() ) - ( Waste * 16 * Config.GetAVMult() ) );
        if( result < 0 ) { result = 0; }
        return result;
    }

    public float GetCoverage() {
        // returns the amount of max armor coverage on this mech as a percentage
        float result = (float) GetArmorValue() / (float) GetMaxArmor();
        return (float) Math.floor( result * 10000 ) / 100;
    }

    public float GetMaxTonnage() {
        // returns the maximum armor tonnage supported by this mech.
        float result = GetMaxArmor() / ( 8 * Config.GetAVMult() );
        int mid = Math.round( result + 0.4999f );
        result = mid * 0.5f;
        return result;
    }

    public float GetAVMult() {
        // convenience method for armor placement
        return Config.GetAVMult();
    }

    public float GetBVTypeMult() {
        return Config.GetBVTypeMult() + Owner.GetTotalModifiers( false, true ).ArmorMultiplier();
    }

    public ifState[] GetStates() {
        ifState[] retval = { (ifState) ISIN, (ifState) ISMS, (ifState) ISFF,
            (ifState) ISST, (ifState) ISLF, (ifState) ISHF, (ifState) ISHA, (ifState) ISLR, (ifState) ISRE, (ifState) ISCM,
            (ifState) CLIN, (ifState) CLMS, (ifState) CLFF, (ifState) CLFL, (ifState) CLHA, (ifState) CLLR, (ifState) CLRE, (ifState) CLCM };
        return retval;
    }

    @Override
    public float GetCost() {
        return GetTonnage() * Config.GetCostMult();
    }

    public float GetOffensiveBV() {
        return 0.0f;
    }

    public float GetCurOffensiveBV( boolean UseRear, boolean UseTC, boolean UseAES ) {
        return GetOffensiveBV();
    }

    public float GetDefensiveBV() {
        return ( GetArmorValue() + GetModularArmorValue() ) * Config.GetBVTypeMult() * 2.5f;
    }

    public int GetBAR() {
        return Config.GetBAR();
    }

    @Override
    public void ResetPlaced() {
        Placed = 0;
    }

    @Override
    public boolean Contiguous() {
        return false;
    }

    @Override
    public boolean LocationLocked() {
        return Config.LocationLocked();
    }

    @Override
    public void SetLocked( boolean l ) {
        Config.SetLocked( l );
    }

    @Override
    public boolean CoreComponent() {
        return true;
    }

    @Override
    public AvailableCode GetAvailability() {
        return Config.GetAvailability();
    }

    @Override
    public boolean IsCritable() {
        return false;
    }

    @Override
    public MechModifier GetMechModifier() {
        return Config.GetMechModifier();
    }

    @Override
    public String toString() {
        if( Config.NumCrits() > 0 ) {
            if( Config.NumCrits() > Placed ) {
                if( Config.IsStealth() ) {
                    return Config.GetCritName();
                } else {
                    return Config.GetCritName() + " (" + ( Config.NumCrits() - Placed ) + ")";
                }
            } else {
                return Config.GetCritName();
            }
        }
        return Config.GetCritName();
    }
}
