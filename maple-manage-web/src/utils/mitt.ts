import mitt, { Emitter } from 'mitt';

const emitter: Emitter<MittType> = mitt<MittType>();

export default emitter;
