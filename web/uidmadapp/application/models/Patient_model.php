<?php
class Patient_model extends CI_Model{
    protected $table = 'PATIENT';
    
    public function __construct()
    {
        parent::__construct();
    }
    public function login($email, $password){
        $retour = $this->db->select('*')
                        ->from($this->table)
                        ->where('EMAIL', $email)
                        ->where('PASSWORD', $password)
                        ->get()->result();
        return $retour;
    }
    public function loginfb($fb_id,$nom,$email,$sexe){
        $retour = $this->db->select('*')
                        ->from($this->table)
                        ->where('FB_ID', $fb_id)
                        ->get()->result();
        if(count($retour)>0){
            return $retour;
        }
        $retour = $this->register($nom, '', $sexe, '', '','', $email, '', '', null);
        return $retour;
    }
    public function register($nom, $prenom, $sexe,$date_naissance,$adresse,$contact,$email,$password, $listemaladie){
         $this->db->insert($this->table, array('NOM' => $nom, 'PRENOM' => $prenom, 'DATE_NAISSANCE' => $date_naissance, 'ADRESSE' => $adresse, 'CONTACT' => $contact, 'EMAIL' => $email, 'PASSWORD' => $password));
        $userid = $this->db->insert_id();
        $retour = array();
        $retour[0] = array(
            'ID_PATIENT' => $userid,
            'NOM' => $nom, 
            'PRENOM' => $prenom, 
            'SEXE' => $sexe, 
            'DATE_NAISSANCE' => $date_naissance, 
            'ADRESSE' => $adresse, 
            'CONTACT' => $contact, 
            'EMAIL' => $email, 
            'PASSWORD' => $password, 
            'FB_ID' => null,
            'PHOTO' => null,
        );
        
        if($listemaladie!=null){
            for($i=0;$i<count($listemaladie);$i++){
                $item = $this->db->select('*')
                        ->from('MALADIE')
                        ->where('TITRE', $listemaladie[$i])
                        ->get()->row();
                
                $this->db->insert('MALADIE_PATIENT', array('PATIENT_ID_PATIENT' => $userid, 'MALADIE_ID_MALADIE' => $item->ID_MALADIE));
            }
        }
        return $retour;
    }
    
    public function update($iduser, $nom, $prenom, $sexe, $date_naissance, $contact, $email, $password, $listemaladie){
        $this->db->update($this->table, array('NOM' => $nom, 'PRENOM' => $prenom, 'DATE_NAISSANCE' => $date_naissance, 'ADRESSE' => $adresse, 'CONTACT' => $contact, 'PASSWORD' => $password));
        $retour = array();
        $retour[0] = array(
            'ID_PATIENT' => $iduser,
            'NOM' => $nom, 
            'PRENOM' => $prenom, 
            'SEXE' => $sexe, 
            'DATE_NAISSANCE' => $date_naissance, 
            'ADRESSE' => $adresse, 
            'CONTACT' => $contact, 
            'EMAIL' => $email, 
            'PASSWORD' => $password, 
            'FB_ID' => null,
            'PHOTO' => null,
        );
        $this->db->delete('MALADIE_PATIENT', array('PATIENT_ID_PATIENT' => $iduser));
        if($listemaladie!=null){
            for($i=0;$i<count($listemaladie);$i++){
                $item = $this->db->select('*')
                        ->from('MALADIE')
                        ->where('TITRE', $listemaladie[$i])
                        ->get()->row();
                
                $this->db->insert('MALADIE_PATIENT', array('PATIENT_ID_PATIENT' => $userid, 'MALADIE_ID_MALADIE' => $item->ID_MALADIE));
            }
        }
        return $retour;
    }
    
    public function registertag($iduser, $tagid){
        $retour = $this->db->select('*')
                        ->from('TAG')
                        ->where('ID_TAG', $tagid)
                        ->get()->result();
        if(count($retour)>0){
            return 'ALREADY_USED';
        }
        $this->db->insert('TAG', array('ID_TAG' => $tagid, 'PATIENT_ID_PATIENT' => $iduser));
        return 'OK';
    }
    
    public function listeMaladie(){
        $retour = $this->db->select('*')
                        ->from('MALADIE')
                        ->get()->result();
        return $retour;
    }
    
    
    public function listeMaladieById($iduser){
        $retour = $this->db->select('*')
                        ->from('MALADIE_USER')
                        ->where('IDUSER',$iduser)
                        ->get()->result();
        
        return $retour;
    }
}