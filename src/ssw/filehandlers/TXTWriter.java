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

package ssw.filehandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import ssw.*;
import ssw.components.*;

public class TXTWriter {

    private Mech CurMech;
    private Options MyOptions;
    private String NL;

    public TXTWriter( Mech m, Options o ) {
        CurMech = m;
        MyOptions = o;
        NL = System.getProperty( "line.separator" );
    }

// This is a text formating string (80 chars) I keep around for when it's needed
//        "----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+"
    public void WriteTXT( String filename ) throws IOException {
        BufferedWriter FR = new BufferedWriter( new FileWriter( filename ) );

        // get the text export and write it
        FR.write( GetTextExport() );

        // all done
        FR.close();
    }

    public String GetTextExport() {
        String retval = "";

        if( CurMech.IsOmnimech() ) {
            // start out with the base chassis.  we'll switch later when needed
            CurMech.SetCurLoadout( Constants.BASELOADOUT_NAME );
        }
        retval += CurMech.GetName() + " " + CurMech.GetModel() + NL + NL;
        switch( CurMech.GetBaseRulesLevel() ) {
        case Constants.TOURNAMENT:
            retval += "Rules Level: Tournament Legal" + NL;
            break;
        case Constants.ADVANCED:
            retval += "Rules Level: Advanced" + NL;
            break;
        case Constants.EXPERIMENTAL:
            retval += "Rules Level: Experimental Tech" + NL;
            break;
        }
        if( CurMech.IsClan() ) {
            retval += "Tech Base: Clan" + NL;
        } else {
            retval += "Tech Base: Inner Sphere" + NL;
        }
        if( CurMech.IsQuad() ) {
            if( CurMech.IsOmnimech() ) {
                retval += "Chassis Config: Quad Omnimech" + NL;
            } else {
                if( CurMech.IsIndustrialmech() ) {
                    retval += "Chassis Config: Quad IndustrialMech" + NL;
                } else {
                    retval += "Chassis Config: Quad" + NL;
                }
            }
        } else {
            if( CurMech.IsOmnimech() ) {
                retval += "Chassis Config: Biped Omnimech" + NL;
            } else {
                if( CurMech.IsIndustrialmech() ) {
                    retval += "Chassis Config: Biped IndustrialMech" + NL;
                } else {
                    retval += "Chassis Config: Biped" + NL;
                }
            }
        }
        retval += "Production Year: " + CurMech.GetYear() + NL;
        retval += "Mass: " + CurMech.GetTonnage() + " tons" + NL + NL;
        retval += "Chassis: " + CurMech.GetChassisModel() + " " + CurMech.GetIntStruc().GetCritName() + NL;
        retval += "Power Plant: " + CurMech.GetEngineManufacturer() + " " + CurMech.GetEngine().GetRating() + " " + CurMech.GetEngine() + NL;
        if( CurMech.GetAdjustedWalkingMP( false, true ) != CurMech.GetWalkingMP() ) {
            retval += "Walking Speed: " + ( CurMech.GetWalkingMP() * 10.75f ) + " km/h (" + ( CurMech.GetAdjustedWalkingMP( false, true ) * 10.75f ) + " km/h" + NL;
        } else {
            retval += "Walking Speed: " + ( CurMech.GetWalkingMP() * 10.75f ) + " km/h" + NL;
        }
        if( CurMech.GetAdjustedRunningMP( false, true ) != CurMech.GetRunningMP() ) {
            retval += "Maximum Speed: " + ( CurMech.GetRunningMP() * 10.75f ) + " km/h (" + ( CurMech.GetAdjustedRunningMP( false, true ) * 10.75f ) + " km/h)" + NL;
        } else {
            retval += "Maximum Speed: " + ( CurMech.GetRunningMP() * 10.75f ) + " km/h" + NL;
        }
        retval += "Jump Jets: " + CurMech.GetJJModel() + NL;
        if( CurMech.GetJumpJets().GetNumJJ() > 0 ) {
            if( CurMech.GetAdjustedJumpingMP( false ) != CurMech.GetJumpJets().GetNumJJ() ) {
                retval += "    Jump Capacity: " + ( CurMech.GetJumpJets().GetNumJJ() * 30 ) + " meters (" + ( CurMech.GetAdjustedJumpingMP( false ) * 30 ) + " meters)" + NL;
            } else {
                retval += "    Jump Capacity: " + ( CurMech.GetJumpJets().GetNumJJ() * 30 ) + " meters" + NL;
            }
        }
        if( CurMech.HasCTCase()|| CurMech.HasLTCase() || CurMech.HasRTCase() ) {
            retval += "Armor: " + CurMech.GetArmorModel() + " " + CurMech.GetArmor().GetCritName() + " w/ CASE" + NL;
        } else {
            retval += "Armor: " + CurMech.GetArmorModel() + " " + CurMech.GetArmor().GetCritName() + NL;
        }
        retval += "Armament:" + NL;
        retval += GetArmament();
        retval += "Manufacturer: " + CurMech.GetCompany() + NL;
        retval += "    Primary Factory: " + CurMech.GetLocation() + NL;
        retval += BuildComputerBlock() + NL + NL;
        retval += "================================================================================" + NL;
        if( ! CurMech.GetOverview().equals( "" ) ) {
            retval += "Overview:" + NL;
            retval += CurMech.GetOverview() + NL + NL;
        }
        if( ! CurMech.GetCapabilities().equals( "" ) ) {
            retval += "Capabilities:" + NL;
            retval += CurMech.GetCapabilities() + NL + NL;
        }
        if( ! CurMech.GetHistory().equals( "" ) ) {
            retval += "Battle History:" + NL;
            retval += CurMech.GetHistory() + NL + NL;
        }
        if( ! CurMech.GetDeployment().equals( "" ) ) {
            retval += "Deployment:" + NL;
            retval += CurMech.GetDeployment() + NL + NL;
        }
        if( ! CurMech.GetVariants().equals( "" ) ) {
            retval += "Variants:" + NL;
            retval += CurMech.GetVariants() + NL + NL;
        }
        if( ! CurMech.GetNotables().equals( "" ) ) {
            retval += "Notable 'Mechs & MechWarriors: " + NL;
            retval += CurMech.GetNotables() + NL + NL;
        }
        retval += "================================================================================" + NL;
        retval += CurMech.GetName() + " " + CurMech.GetModel() + NL + NL;
        if( CurMech.IsClan() ) {
            retval += "Tech Base: Clan" + NL;
        } else {
            retval += "Tech Base: Inner Sphere" + NL;
        }
        if( CurMech.IsQuad() ) {
            if( CurMech.IsOmnimech() ) {
                retval += "Chassis Config: Quad Omnimech" + NL;
            } else {
                if( CurMech.IsIndustrialmech() ) {
                    retval += "Chassis Config: Quad IndustrialMech" + NL;
                } else {
                    retval += "Chassis Config: Quad" + NL;
                }
            }
        } else {
            if( CurMech.IsOmnimech() ) {
                retval += "Chassis Config: Biped Omnimech" + NL;
            } else {
                if( CurMech.IsIndustrialmech() ) {
                    retval += "Chassis Config: Biped IndustrialMech" + NL;
                } else {
                    retval += "Chassis Config: Biped" + NL;
                }
            }
        }
        retval += String.format( "Era: %1$-56s Cost: %2$,.0f", DecodeEra(), Math.floor( CurMech.GetTotalCost() + 0.5f ) ) + NL;
        retval += String.format( "Availability: %1$-48s BV2: %2$,d", CurMech.GetAvailability().GetShortenedCode(), CurMech.GetCurrentBV() ) + NL + NL;
        retval += "Equipment           Type                         Rating                   Mass  " + NL;
        retval += "--------------------------------------------------------------------------------" + NL;
        retval += String.format( "Internal Structure: %1$-28s %2$3s points              %3$6.2f", CurMech.GetIntStruc().GetCritName(), CurMech.GetIntStruc().GetTotalPoints(), CurMech.GetIntStruc().GetTonnage() ) + NL;
        if( CurMech.GetIntStruc().NumCrits() > 0 ) {
            retval += "    Internal Locations: " + FileCommon.GetInternalLocations( CurMech ) + NL;
        }
        retval += String.format( "Engine:             %1$-28s %2$3s                     %3$6.2f", CurMech.GetEngine().GetCritName(), CurMech.GetEngine().GetRating(), CurMech.GetEngine().GetTonnage() ) + NL;
        retval += "    Walking MP: " + CurMech.GetWalkingMP() + NL;
        if( CurMech.GetRunningMP() != CurMech.GetAdjustedRunningMP( false, true ) ) {
            retval += "    Running MP: " + CurMech.GetRunningMP() + " (" + CurMech.GetAdjustedRunningMP( false, true ) + ")" + NL;
        } else {
            retval += "    Running MP: " + CurMech.GetRunningMP() + NL;
        }
        retval += "    Jumping MP: " + CurMech.GetJumpJets().GetNumJJ();
        if( CurMech.GetJumpJets().GetNumJJ() > 0 ) {
            if( CurMech.GetJumpJets().IsImproved() ) {
                retval += "  (Improved)";
            } else {
                retval += "  (Standard)";
            }
        }
        retval += NL;
        if( CurMech.GetJumpJets().GetNumJJ() > 0 ) {
            retval += String.format( "    %1$-68s %2$6.2f", "Jump Jet Locations: " + FileCommon.GetJumpJetLocations( CurMech ), CurMech.GetJumpJets().GetTonnage() ) + NL;
        }
        retval += String.format( "Heat Sinks:         %1$-28s %2$-8s                %3$6.2f", GetHSType(), GetHSNum(), CurMech.GetHeatSinks().GetTonnage() ) + NL;
        if( CurMech.GetHeatSinks().GetNumHS() > CurMech.GetEngine().InternalHeatSinks() ) {
            retval += "    Heat Sink Locations: " + FileCommon.GetHeatSinkLocations( CurMech ) + NL;
        }
        retval += String.format( "Gyro:               %1$-52s %2$6.2f", CurMech.GetGyro().GetLookupName(), CurMech.GetGyro().GetTonnage() ) + NL;
        retval += String.format( "Cockpit:            %1$-52s %2$6.2f", CurMech.GetCockpit().GetLookupName(), CurMech.GetCockpit().GetTonnage() ) + NL;
        if( ! CurMech.GetEngine().IsNuclear() ) {
            if( CurMech.GetLoadout().GetPowerAmplifier().GetTonnage() > 0 ) {
                retval += String.format( "%1$-72s %2$6.2f", "Power Amplifiers:", CurMech.GetLoadout().GetPowerAmplifier().GetTonnage() ) + NL;
            }
        }
        retval += "    Actuators:      " + FileCommon.BuildActuators( CurMech, false ) + NL;
        if( CurMech.GetPhysEnhance().IsTSM() ) {
            retval += "    TSM Locations: " + FileCommon.GetTSMLocations( CurMech ) + NL;
        }
        if( CurMech.GetArmor().GetBAR() < 10 ) {
            retval += String.format( "Armor:              %1$-28s AV - %2$3s                %3$6.2f", CurMech.GetArmor().GetCritName() + " (B.A.R.: " + CurMech.GetArmor().GetBAR() +")", CurMech.GetArmor().GetArmorValue(), CurMech.GetArmor().GetTonnage() ) + NL;
        } else {
            retval += String.format( "Armor:              %1$-28s AV - %2$3s                %3$6.2f", CurMech.GetArmor().GetCritName(), CurMech.GetArmor().GetArmorValue(), CurMech.GetArmor().GetTonnage() ) + NL;
        }
        if( CurMech.GetArmor().NumCrits() > 0 ) {
            retval += "    Armor Locations: " + FileCommon.GetArmorLocations( CurMech ) + NL;
        }
        if( CurMech.GetCaseTonnage() != 0.0f ) {
            retval += String.format( "    %1$-68s %2$6.2f", "CASE Locations: " + FileCommon.GetCaseLocations( CurMech ), CurMech.GetCaseTonnage() ) + NL;
        }
        if( CurMech.GetCASEIITonnage() != 0.0f ) {
            retval += String.format( "    %1$-68s %2$6.2f", "CASE II Locations: " + FileCommon.GetCaseIILocations( CurMech ), CurMech.GetCASEIITonnage() ) + NL;
        }
        retval += NL + "                                                      Internal       Armor      " + NL;
        retval += "                                                      Structure      Factor     " + NL;
        retval += String.format( "                                                Head     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetHeadPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_HD ) ) + NL;
        retval += String.format( "                                        Center Torso     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetCTPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_CT ) ) + NL;
        retval += String.format( "                                 Center Torso (rear)                  %1$-3s       ", CurMech.GetArmor().GetLocationArmor( Constants.LOC_CTR ) ) + NL;
        if( CurMech.GetArmor().GetLocationArmor( Constants.LOC_LT ) != CurMech.GetArmor().GetLocationArmor( Constants.LOC_RT ) ) {
            retval += String.format( "                                          Left Torso     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetSidePoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LT ) ) + NL;
            retval += String.format( "                                         Right Torso     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetSidePoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_RT ) ) + NL;
        } else {
            retval += String.format( "                                           L/R Torso     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetSidePoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LT ) ) + NL;
        }
        if( CurMech.GetArmor().GetLocationArmor( Constants.LOC_LTR ) != CurMech.GetArmor().GetLocationArmor( Constants.LOC_RTR ) ) {
            retval += String.format( "                                   Left Torso (rear)                  %1$-3s       ", CurMech.GetArmor().GetLocationArmor( Constants.LOC_LTR ) ) + NL;
            retval += String.format( "                                  Right Torso (rear)                  %1$-3s       ", CurMech.GetArmor().GetLocationArmor( Constants.LOC_RTR ) ) + NL;
        } else {
            retval += String.format( "                                    L/R Torso (rear)                  %1$-3s       ", CurMech.GetArmor().GetLocationArmor( Constants.LOC_LTR ) ) + NL;
        }
        if( CurMech.GetArmor().GetLocationArmor( Constants.LOC_LA ) != CurMech.GetArmor().GetLocationArmor( Constants.LOC_RA ) ) {
            if( CurMech.IsQuad() ) {
                retval += String.format( "                                      Left Front Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LA ) ) + NL;
                retval += String.format( "                                     Right Front Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_RA ) ) + NL;
            } else {
                retval += String.format( "                                            Left Arm     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LA ) ) + NL;
                retval += String.format( "                                           Right Arm     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_RA ) ) + NL;
            }
        } else {
            if( CurMech.IsQuad() ) {
                retval += String.format( "                                       L/R Front Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LA ) ) + NL;
            } else {
                retval += String.format( "                                             L/R Arm     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetArmPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LA ) ) + NL;
            }
        }
        if( CurMech.GetArmor().GetLocationArmor( Constants.LOC_LL ) != CurMech.GetArmor().GetLocationArmor( Constants.LOC_RL ) ) {
            if( CurMech.IsQuad() ) {
                retval += String.format( "                                       Left Rear Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LL ) ) + NL;
                retval += String.format( "                                      Right Rear Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_RL ) ) + NL;
            } else {
                retval += String.format( "                                            Left Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LL ) ) + NL;
                retval += String.format( "                                           Right Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_RL ) ) + NL;
            }
        } else {
            if( CurMech.IsQuad() ) {
                retval += String.format( "                                        L/R Rear Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LL ) ) + NL;
            } else {
                retval += String.format( "                                             L/R Leg     %1$-3s          %2$-3s       ", CurMech.GetIntStruc().GetLegPoints(), CurMech.GetArmor().GetLocationArmor( Constants.LOC_LL ) ) + NL;
            }
        }
        if( CurMech.IsOmnimech() ) {
            Vector l = CurMech.GetLoadouts();
            for( int i = 0; i < l.size(); i++ ) {
                CurMech.SetCurLoadout( ((ifLoadout) l.get( i )).GetName() );
                retval += NL + "================================================================================" + NL;
                retval += BuildOmniLoadout() + NL;
            }
        } else {
            retval += NL + "================================================================================" + NL;
            retval += BuildEquipmentBlock() + NL;
        }

        return retval;
    }

    private String GetArmament() {
        Vector v = CurMech.GetLoadout().GetNonCore();
        Vector w = new Vector();
        Vector EQ = new Vector();
        String Armament = "";

        // find only the armaments
        for( int i = 0; i < v.size(); i++ ) {
            if( v.get( i ) instanceof ifWeapon ) {
                w.add( v.get( i ) );
            } else {
                EQ.add( v.get( i ) );
            }
        }

        // do we need to continue?
        if( w.size() <= 0 ) {
            if( CurMech.IsOmnimech() ) {
                return "    " + ( CurMech.GetTonnage() - CurMech.GetCurrentTons() ) + " tons of pod space." + NL;
            } else {
                return "    None" + NL;
            }
        }

        // sort the weapons according to current BV, assuming front facing
        abPlaceable[] weapons = CurMech.SortWeapons( w, false );
        Vector Temp = new Vector();

        // add in the rest of the equipment
        for( int i = 0; i < weapons.length; i++ ) {
            Temp.add( weapons[i] );
        }
        for( int i = 0; i < EQ.size(); i++ ) {
            if( ! ( EQ.get( i ) instanceof Ammunition ) ) {
                Temp.add( EQ.get( i ) );
            }
        }
        weapons = new abPlaceable[Temp.size()];
        for( int i = 0; i < Temp.size(); i++ ) {
            weapons[i] = (abPlaceable) Temp.get( i );
        }

        // initialize the next loop
        int numthistype = 1;
        abPlaceable cur = weapons[0];
        weapons[0] = null;

        // do we need to continue?
        if( weapons.length <= 1 ) {
            if( cur instanceof MissileWeapon ) {
                if( ((MissileWeapon) cur).IsUsingArtemis() ) {
                    Armament = "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + " w/ Artemis IV FCS" + NL;
                } else {
                    Armament = "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + NL;
                }
            } else {
                Armament = "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + NL;
            }
            if( CurMech.IsOmnimech() ) {
                Armament += "    " + ( CurMech.GetTonnage() - CurMech.GetCurrentTons() ) + " tons of pod space." + NL;
            }
            return Armament;
        }

        // count up individual weapons and build their string
        String plural = "";
        for( int i = 1; i <= weapons.length; i++ ) {
            // find any other weapons of this type
            for( int j = 0; j < weapons.length; j++ ) {
                if( weapons[j] != null ) {
                    if( weapons[j].GetCritName().matches( cur.GetCritName() ) && weapons[j].GetManufacturer().matches( cur.GetManufacturer() ) ) {
                        numthistype++;
                        weapons[j] = null;
                    }
                }
            }

            // add the current weapon to the armament string
            if( numthistype > 1 ) {
                plural = "s";
            } else {
                plural = "";
            }

            if( cur instanceof MissileWeapon ) {
                if( ((MissileWeapon) cur).IsUsingArtemis() ) {
                        Armament += "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + plural + " w/ Artemis IV FCS" + NL;
                } else {
                    Armament += "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + plural + NL;
                }
            } else {
                Armament += "    " + numthistype + " " + cur.GetManufacturer() + " " + cur.GetCritName() + plural + NL;
            }

            // find the next weapon type and set it to current
            cur = null;
            numthistype = 0;
            for( int j = 0; j < weapons.length; j++ ) {
                if( weapons[j] != null ) {
                    cur = weapons[j];
                    weapons[j] = null;
                    numthistype = 1;
                    break;
                }
            }

            // do we need to continue?
            if( cur == null ) { break; }
        }

        if( CurMech.IsOmnimech() ) {
            Armament += "    " + ( CurMech.GetTonnage() - CurMech.GetCurrentTons() ) + " tons of pod space." + NL;
        }

        // all done
        return Armament;
    }

    private String DecodeEra() {
        // sends the correct era string for the mech
        switch( CurMech.GetEra() ) {
        case Constants.STAR_LEAGUE:
            return Constants.strSTAR_LEAGUE;
        case Constants.SUCCESSION:
            return Constants.strSUCCESSION;
        case Constants.CLAN_INVASION:
            return Constants.strCLAN_INVASION;
        case Constants.ALL_ERA:
            return Constants.strALL_ERA;
        default:
            return "Unknown";
        }
    }

    private String GetHSType() {
        if( CurMech.GetHeatSinks().IsDouble() ) {
            return "Double";
        } else {
            return "Single";
        }
    }

    private String GetHSNum() {
        // provides a formated heat sink dissipation string
        if( CurMech.GetHeatSinks().IsDouble() ) {
            return CurMech.GetHeatSinks().GetNumHS() + "(" + CurMech.GetHeatSinks().TotalDissipation() + ")";
        } else {
            return "" + CurMech.GetHeatSinks().GetNumHS();
        }
    }

    private String BuildEquipmentBlock() {
        // this routine builds the big equipment block at the bottom of the file
        String retval = "Equipment                                        Location     Critical    Mass  " + NL;
        retval += "--------------------------------------------------------------------------------" + NL;
        String loc = "";
        String crits = "";
        Vector v = (Vector) CurMech.GetLoadout().GetNonCore().clone();

        // add in MASC and the targeting computer if needed.
        if( CurMech.GetPhysEnhance().IsMASC() ) {
            v.add( CurMech.GetPhysEnhance() );
        }
        if( CurMech.UsingTC() ) {
            v.add( CurMech.GetTC() );
        }
        if( CurMech.GetLoadout().HasSupercharger() ) {
            v.add( CurMech.GetLoadout().GetSupercharger() );
        }

        // now sort the equipment by location
        v = FileCommon.SortEquipmentForStats( CurMech, v, MyOptions );

        for( int i = 0; i < v.size(); i++ ) {
            // get the equipment and find out where it lives
            abPlaceable p = (abPlaceable) v.get( i );

            // is is splittable and does it have more than one location?
            if( p.CanSplit() ) {
                int[] check = CurMech.GetLoadout().FindInstances( p );
                loc = FileCommon.EncodeLocations( check, CurMech.IsQuad() );
                crits = FileCommon.DecodeCrits( check );
            } else {
                loc = FileCommon.EncodeLocation( CurMech.GetLoadout().Find( p ), CurMech.IsQuad() );
                crits = "" + p.NumCrits();
            }

            // build the string based on the type of equipment
            if( p instanceof Ammunition ) {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", ((Ammunition) p).GetPrintName(), loc, crits, p.GetTonnage() ) + NL;
            } else if( p instanceof MissileWeapon ) {
                if( ((MissileWeapon) p).IsUsingArtemis() ) {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() - 1.0f ) + NL;
                    abPlaceable a = ((MissileWeapon) p).GetArtemis();
                    retval += String.format( "    %1$-46s %2$-13s %3$-7s %4$6.2f", a.GetCritName(), loc, a.NumCrits(), 1.0f ) + NL;
                } else {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() ) + NL;
                }
            } else if ( p instanceof MGArray ) {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, ((MGArray) p).GetBaseTons() ) + NL;
                abPlaceable a = ((MGArray) p).GetMGType();
                retval += String.format( "    %1$-46s %2$-13s %3$-7s %4$6.2f", ((MGArray) p).GetNumMGs() + " " + a.GetCritName(), loc, ((MGArray) p).GetNumMGs(), ( ((MGArray) p).GetMGTons() * ((MGArray) p).GetNumMGs() ) ) + NL;
            } else if( p instanceof EnergyWeapon ) {
                if( ((EnergyWeapon) p).HasCapacitor() ) {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() - 1.0f ) + NL;
                    abPlaceable a = ((EnergyWeapon) p).GetCapacitor();
                    retval += String.format( "    %1$-46s %2$-13s %3$-7s %4$6.2f", a.GetCritName(), loc, a.NumCrits(), a.GetTonnage() ) + NL;
                } else {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() ) + NL;
                }
            } else {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() ) + NL;
            }
        }

        // add in any special systems
        boolean Special = false;
        if( CurMech.HasNullSig() ) {
            retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", CurMech.GetNullSig().GetCritName(), "*", 7, 0.0f ) + NL;
            Special = true;
        }
        if( CurMech.HasVoidSig() ) {
            retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", CurMech.GetVoidSig().GetCritName(), "*", 7, 0.0f ) + NL;
            Special = true;
        }
        if( CurMech.HasChameleon() ) {
            retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", CurMech.GetChameleon().GetCritName(), "*", 6, 0.0f ) + NL;
            Special = true;
        }
        if( CurMech.HasBlueShield() ) {
            retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", CurMech.GetBlueShield().GetCritName(), "*", 7, 3.0f ) + NL;
            Special = true;
        }

        if( Special ) {
            retval += NL;
        }

        // add in the equipment footers if needed
        if( CurMech.HasNullSig() ) {
            retval += "* The " + CurMech.GetNullSig().GetCritName() + " occupies 1 slot in every location except the HD." + NL;
        }
        if( CurMech.HasVoidSig() ) {
            retval += "* The " + CurMech.GetVoidSig().GetCritName() + " occupies 1 slot in every location except the HD." + NL;
        }
        if( CurMech.HasChameleon() ) {
            retval += "* The " + CurMech.GetChameleon().GetCritName() + " occupies 1 slot in every location except the HD and CT." + NL;
        }
        if( CurMech.HasBlueShield() ) {
            retval += "* The " + CurMech.GetBlueShield().GetCritName() + " occupies 1 slot in every location except the HD." + NL;
        }

        return retval;
    }

    private String BuildOmniLoadout() {
        // this routine builds the big equipment block at the bottom of the file
        String retval = "";
        String loc = "";
        String crits = "";
        Vector v = (Vector) CurMech.GetLoadout().GetNonCore().clone();

        if( CurMech.GetBaseRulesLevel() != CurMech.GetLoadout().GetRulesLevel() ) {
            switch( CurMech.GetLoadout().GetRulesLevel() ) {
            case Constants.TOURNAMENT:
                retval += "Rules Level: Tournament Legal" + NL;
                break;
            case Constants.ADVANCED:
                retval += "Rules Level: Advanced" + NL;
                break;
            case Constants.EXPERIMENTAL:
                retval += "Rules Level: Experimental Tech" + NL;
                break;
            }
        }
        retval += String.format( "Loadout: %1$-52s Cost: %2$,.0f", CurMech.GetLoadout().GetName(), Math.floor( CurMech.GetTotalCost() + 0.5f ) ) + NL;
        retval += String.format( "Availability: %1$-48s BV2: %2$,d", CurMech.GetAvailability().GetShortenedCode(), CurMech.GetCurrentBV() ) + NL;

        // build the starting block for the loadout information
        retval += NL + "Equipment           Type                         Rating                   Mass  " + NL;
        retval += "--------------------------------------------------------------------------------" + NL;
        if( CurMech.GetJumpJets().GetNumJJ() > 0 ) {
            if( CurMech.GetJumpJets().IsImproved() ) {
                retval += "    Jumping MP: " + CurMech.GetJumpJets().GetNumJJ() + "  (Improved)" + NL;
            } else {
                retval += "    Jumping MP: " + CurMech.GetJumpJets().GetNumJJ() + "  (Standard)" + NL;
            }
        }
        if( CurMech.GetJumpJets().GetNumJJ() > 0 ) {
            retval += String.format( "    %1$-68s %2$6.2f", "Jump Jet Locations: " + FileCommon.GetJumpJetLocations( CurMech ), CurMech.GetJumpJets().GetOmniTonnage() ) + NL;
        }
        if( CurMech.GetHeatSinks().GetNumHS() > CurMech.GetHeatSinks().GetBaseLoadoutNumHS() ) {
            retval += String.format( "Heat Sinks:         %1$-28s %2$-8s                %3$6.2f", GetHSType(), GetHSNum(), CurMech.GetHeatSinks().GetOmniTonnage() ) + NL;
            if( CurMech.GetHeatSinks().GetNumHS() > CurMech.GetEngine().InternalHeatSinks() ) {
                retval += "    Heat Sink Locations: " + FileCommon.GetHeatSinkLocations( CurMech ) + NL;
            }
        }
        if( CurMech.GetCaseTonnage() != 0.0f ) {
            retval += String.format( "    %1$-68s %2$6.2f", "CASE Locations: " + FileCommon.GetCaseLocations( CurMech ), CurMech.GetCaseTonnage() ) + NL;
        }
        if( CurMech.GetCASEIITonnage() != 0.0f ) {
            retval += String.format( "    %1$-68s %2$6.2f", "CASE II Locations: " + FileCommon.GetCaseIILocations( CurMech ), CurMech.GetCASEIITonnage() ) + NL;
        }
        if( ! CurMech.GetEngine().IsNuclear() ) {
            if( CurMech.GetLoadout().GetPowerAmplifier().GetTonnage() > 0 ) {
                retval += String.format( "%1$-72s %2$6.2f", "Power Amplifiers:", CurMech.GetLoadout().GetPowerAmplifier().GetTonnage() ) + NL;
            }
        }
        retval += "    Actuators:      " + FileCommon.BuildActuators( CurMech, false ) + NL;

        // add in MASC and the targeting computer if needed.
        if( CurMech.GetPhysEnhance().IsMASC() ) {
            v.add( CurMech.GetPhysEnhance() );
        }
        if( CurMech.UsingTC() ) {
            v.add( CurMech.GetTC() );
        }
        if( CurMech.GetLoadout().HasSupercharger() ) {
            v.add( CurMech.GetLoadout().GetSupercharger() );
        }

        // now sort the equipment by location
        v = FileCommon.SortEquipmentForStats( CurMech, v, MyOptions );

        // the basic equipment block header
        retval += NL + "Equipment                                        Location     Critical    Mass  " + NL;

        for( int i = 0; i < v.size(); i++ ) {
            // get the equipment and find out where it lives
            abPlaceable p = (abPlaceable) v.get( i );

            // is is splittable and does it have more than one location?
            if( p.CanSplit() ) {
                int[] check = CurMech.GetLoadout().FindInstances( p );
                loc = FileCommon.EncodeLocations( check, CurMech.IsQuad() );
                crits = FileCommon.DecodeCrits( check );
            } else {
                loc = FileCommon.EncodeLocation( CurMech.GetLoadout().Find( p ), CurMech.IsQuad() );
                crits = "" + p.NumCrits();
            }

            // build the string based on the type of equipment
            if( p instanceof Ammunition ) {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", ((Ammunition) p).GetPrintName(), loc, crits, p.GetTonnage() ) + NL;
            } else if( p instanceof MissileWeapon ) {
                if( ((MissileWeapon) p).IsUsingArtemis() ) {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() - 1.0f ) + NL;
                    abPlaceable a = ((MissileWeapon) p).GetArtemis();
                    retval += String.format( "    %1$-46s %2$-13s %3$-7s %4$6.2f", a.GetCritName(), loc, a.NumCrits(), 1.0f ) + NL;
                } else {
                    retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() ) + NL;
                }
            } else if ( p instanceof MGArray ) {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, ((MGArray) p).GetBaseTons() ) + NL;
                abPlaceable a = ((MGArray) p).GetMGType();
                retval += String.format( "    %1$-46s %2$-13s %3$-7s %4$6.2f", ((MGArray) p).GetNumMGs() + " " + a.GetCritName(), loc, ((MGArray) p).GetNumMGs(), ( ((MGArray) p).GetMGTons() * ((MGArray) p).GetNumMGs() ) ) + NL;
            } else {
                retval += String.format( "%1$-50s %2$-13s %3$-7s %4$6.2f", p.GetCritName(), loc, crits, p.GetTonnage() ) + NL;
            }
        }

        return retval;
    }

    private String BuildComputerBlock() {
        String retval = "";
/*
        abPlaceable ECM = FileCommon.HasECM( CurMech );
        abPlaceable BAP = FileCommon.HasBAP( CurMech );
        Object[] C3 = FileCommon.HasC3( CurMech );
*/
        // start communications system line
        retval += "Communications System: " + CurMech.GetCommSystem() + NL;
/*
        if( ! ( ECM instanceof EmptyItem ) ) {
            retval += NL + "    w/ " + ECM.GetManufacturer() + " " + ECM.GetCritName(); 
            if( C3 != null ) {
                for( int i = 0; i < C3.length; i++ ) {
                    retval += NL + "    and " + ((abPlaceable) C3[i]).GetManufacturer() + " " + ((abPlaceable) C3[i]).GetCritName();
                }
            }
        } else {
            if( C3 != null ) {
                for( int i = 0; i < C3.length; i++ ) {
                    if( i == 0 ) {
                        retval += NL + "    w/ " + ((abPlaceable) C3[i]).GetManufacturer() + " " + ((abPlaceable) C3[i]).GetCritName();
                    } else {
                        retval += NL + "    and " + ((abPlaceable) C3[i]).GetManufacturer() + " " + ((abPlaceable) C3[i]).GetCritName();
                    }
                }
            }
        }
        retval += NL;
*/
        // start targeting and tracking system line
        retval += "Targeting and Tracking System: " + CurMech.GetTandTSystem();
/*        if( ! ( BAP instanceof EmptyItem ) ) {
            if( CurMech.UsingTC() ) {
                retval += NL + "    w/ " + BAP.GetManufacturer() + " " + BAP.GetCritName() + NL + "    and " + CurMech.GetTC().GetCritName();
            } else {
                retval += NL + "    w/ " + BAP.GetManufacturer() + " " + BAP.GetCritName();
            }
        } else {
            if( CurMech.UsingTC() ) {
                retval += NL + "    w/ " + CurMech.GetTC().GetCritName();
            }
        }
*/
        return retval;
    }
}
