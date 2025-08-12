export interface AuthenticationResponse {
    accessToken?: string ;
    refreshToken?:string
    mfaEnabled?: string;
    secretImageUri?: string;
    id?: number,
    firstname?: string,
    lastname?: string,
    email?: string,
    role?: string,
    sexe?:String,
    isEnableds?:String
  }
  