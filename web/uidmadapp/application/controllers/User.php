<?php
/**
 * Created by PhpStorm.
 * User: Ntsou
 * Date: 12/04/2016
 * Time: 15:06
 */
require APPPATH . '/libraries/REST_Controller.php';
class User extends REST_Controller{
    function __construct()
    {
        parent::__construct();
        $this->load->model('Patient_model', 'patient');
    }
    public function user_get()
    {
        $this->response("iooo", REST_Controller::HTTP_OK); // OK (200) being the HTTP response code
    }
    public function login_post(){
        $retour = $this->patient->login($this->post('email'),$this->post('password'));
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }
    public function loginfb_post(){
        $retour = $this->patient->loginfb($this->post('fb_id'),$this->post('nom'),$this->post('email'),$this->post('sexe'));
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }
    
    public function register_post(){
        $nom = $this->post('nom');
        $prenom = $this->post('prenom');
        $sexe = $this->post('sexe');
        $datenaissance = $this->post('datenaissance');
        $adresse = $this->post('adresse');
        $contact = $this->post('contact');
        $email = $this->post('email');
        $motdepasse = $this->post('motdepasse');
        $taille = intval($this->post('taille'));
        $listemaladie = array();
        for($i=0;$i<$taille;$i++){
            $listemaladie[] = $this->post('maladie'.$i);
        }
        
        $retour = $this->patient->register($nom, $prenom, $sexe,$datenaissance,$adresse,$contact,$email,$motdepasse, $listemaladie);
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }
    
    public function updateuser_post(){
        $nom = $this->post('nom');
        $prenom = $this->post('prenom');
        $sexe = $this->post('sexe');
        $datenaissance = $this->post('datenaissance');
        $adresse = $this->post('adresse');
        $contact = $this->post('contact');
        $email = $this->post('email');
        $motdepasse = $this->post('motdepasse');
        $taille = intval($this->post('taille'));
        $listemaladie = array();
        for($i=0;$i<$taille;$i++){
            $listemaladie[] = $this->post('maladie'.$i);
        }
        
        $retour = $this->patient->update($nom, $prenom, $sexe,$datenaissance,$adresse,$contact,$email,$motdepasse, $listemaladie);
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }
    
    public function registertag_post(){
        $iduser = $this->post('iduser');
        $idtag = $this->post('idtag');
        
        $retour = $this->patient->registertag($iduser, $idtag);
        $this->response($retour, REST_Controller::HTTP_OK);
    }
    
    public function listemaladie_post(){
        $retour = $this->patient->listeMaladie();
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }   
    
    public function listemaladiebyid_post(){
        $retour = $this->patient->listeMaladieById($this->post('iduser'));
        $this->response($this->objectToArrayKeyLower($retour), REST_Controller::HTTP_OK);
    }
} 