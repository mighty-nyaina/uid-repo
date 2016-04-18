<?php
/**
 * Created by PhpStorm.
 * User: Ntsou
 * Date: 12/04/2016
 * Time: 19:00
 */
defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';
class Urgence extends REST_Controller{
    function __construct()
    {
        // Construct the parent class
        parent::__construct();
    }
    public function urgence_get()
    {
        $tagid = $this->get('idtag');
        $longitude = $this->get("longitude");;
        $latitude = $this->get("latitude");;
        $v = ['teste '=>$tagid];

        $this->load->model('patient');
        $data['hopital_proche'] = $this->patient->findNearestHospital($longitude,$latitude);
        $patient_info = $this->patient->getInfoClientByTag($tagid);
        $data['info_client'] = $patient_info;
        $data['maladie'] = $this->patient->getMaladieClient($patient_info->ID_PATIENT);
        $this->response($data, REST_Controller::HTTP_OK);
        //$this->response($data, REST_Controller::HTTP_OK);
    }
    public function urgence_post()
    {
        $tagid = $this->post('idtag');

        $longitude = doubleval($this->post("longitude"));
        $latitude = doubleval($this->post("latitude"));

        $this->load->model('patient');
        $patient_info = $this->patient->getInfoClientByTag($tagid);
        if(count($patient_info)>0){
            $data['maladie'] = $this->objectToArrayKeyLower($this->patient->getMaladieClient($patient_info[0]->ID_PATIENT));
            $data['hopital_proche'] = $this->patient->findNearestHospital($longitude,$latitude);
            $data['info_client'] = $this->objectToArrayKeyLower($patient_info);
            $data['longitude'] = $longitude;
            $data['latitude'] = $latitude;
            
            $maladie = $data['maladie'];
            $hopital = $data['hopital_proche'];
            
            $maladie_text = "";
            foreach($maladie as $m)
            {
                $maladie_text = $maladie_text." ".$m['titre'];
            }

            $date = new DateTime($patient_info[0]->DATE_NAISSANCE);

            $this->load->library('email');
            $this->load->library('parser');

            $this->email->from('uid-app@uid-appli.esy.es', 'UID');
            $this->email->to($hopital[0]->email);

            $this->email->subject('URGENCE');

            $data_email = array(
                'nom' => $patient_info[0]->NOM,
                'prenom' => $patient_info[0]->PRENOM,
                'date_naissance' => $date->format('d/m/Y'),
                'maladie_text' => $maladie_text,
                'latitude' => $latitude,
                'longitude' => $longitude,
            );
            $body = $this->parser->parse('email_template', $data_email, true);
            $this->email->message($body);
            $this->email->send();
            
            $this->response($data, REST_Controller::HTTP_OK);
        }else{
            $this->response(null, REST_Controller::HTTP_OK);
        }
    }
}